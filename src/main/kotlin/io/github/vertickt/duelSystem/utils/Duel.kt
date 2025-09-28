package io.github.vertickt.duelSystem.utils

import net.axay.kspigot.chat.KColors
import net.axay.kspigot.event.listen
import net.axay.kspigot.event.unregister
import net.axay.kspigot.extensions.broadcast
import net.axay.kspigot.extensions.server
import net.axay.kspigot.runnables.task
import net.kyori.adventure.title.Title
import net.kyori.adventure.title.TitlePart
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.WorldCreator
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.ItemStack
import org.codehaus.plexus.util.FileUtils
import java.io.File
import java.util.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class Duel(
    val p1: Player,
    val p2: Player
) {

    val duelID = UUID.randomUUID().toString()
    val duelWorldName = "duel-$duelID"
    var time = Duration.ZERO
    val players = listOf(p1, p2)

    val onDamage = listen<EntityDamageEvent> {
        if (cancelTimer) {
            it.isCancelled = true
            return@listen
        }
        val victim = it.entity
        if (victim != p1 && victim != p2) return@listen
        if ((victim as Player).health - it.finalDamage > 0) return@listen
        it.damage = 0.0
        var winner = when (victim) {
            p1 -> p2
            p2 -> p1
            else -> return@listen
        }
        players.forEach { p ->
            win(p, winner)
        }
        stop()
    }

    val onQuit = listen<PlayerQuitEvent> {
        val player = it.player
        if (player != p1 && player != p2) return@listen
        var winner = when (player) {
            p1 -> p2
            p2 -> p1
            else -> return@listen
        }
        players.forEach { p ->
            win(p, winner)
        }
        stop()
    }

    fun ofSeconds(long: Long): java.time.Duration {
        return java.time.Duration.ofSeconds(long)
    }

    fun win(player: Player, winner: Player) {
        player.inventory.clear()
        player.sendTitlePart(TitlePart.TIMES, Title.Times.times(ofSeconds(0), ofSeconds(3), ofSeconds(1)))
        player.sendTitlePart(TitlePart.SUBTITLE, cmp("hat gewonnen", KColors.ALICEBLUE))
        player.sendTitlePart(TitlePart.TITLE, cmp(winner.name, KColors.ALICEBLUE, true))
        player.playSound(player.location, Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1f)
    }


    fun startDuel() {
        FileUtils.copyDirectoryStructure(File("duel-template"), File(duelWorldName))
        server.createWorld(WorldCreator.name(duelWorldName))

        p1.teleportAsync(Location(server.getWorld(duelWorldName), 10.5, 100.0, 0.5, 90f, 0f))
        p2.teleportAsync(Location(server.getWorld(duelWorldName), -10.5, 100.0, 0.5, -90f, 0f))

        timer()
    }

    var cancelTimer = false
    fun timer() {
        task(true, 0, 20) {
            if (cancelTimer) it.cancel()
            players.forEach { it.sendActionBar(cmp(time.toString(), KColors.ALICEBLUE, true)) }
            time -= 1.seconds
            if (time <= 0.seconds) {
                stop()
                it.cancel()
            }
        }
    }

    fun stop() {
        cancelTimer = true
        task(delay = 20 * 6) {
            players.forEach { p ->
                p.teleportAsync(Location(server.getWorld("world"), 0.5, 100.0, 0.5, 0f, 0f))
                resetPlayer(p)
            }
            server.unloadWorld(server.getWorld(duelWorldName) ?: return@task, false)
            FileUtils.deleteDirectory(duelWorldName)
            onDamage.unregister()
            onQuit.unregister()
        }
    }



    init {
        broadcast("Starting duel between ${p1.name} and ${p2.name}")
        time = 5.minutes
        startDuel()
    }
}