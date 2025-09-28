package io.github.vertickt.duelSystem.listeners

import io.github.vertickt.duelSystem.utils.resetPlayer
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.server
import org.bukkit.Location
import org.bukkit.event.player.PlayerJoinEvent

object OnJoin {
    val onJoin = listen<PlayerJoinEvent> {
        val player = it.player
        player.teleportAsync(Location(server.getWorld("world"), 0.5, 100.0, 0.5, 0f, 0f))
        resetPlayer(player)
    }
}