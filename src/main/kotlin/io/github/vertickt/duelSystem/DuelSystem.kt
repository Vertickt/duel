package io.github.vertickt.duelSystem

import io.github.vertickt.duelSystem.commands.DuelCommand
import io.github.vertickt.duelSystem.commands.GetKitCommand
import io.github.vertickt.duelSystem.commands.SetKitCommand
import io.github.vertickt.duelSystem.guis.SpectateGui
import io.github.vertickt.duelSystem.listeners.OnJoin
import net.axay.kspigot.main.KSpigot
import org.bukkit.Difficulty


class DuelSystem : KSpigot() {
    companion object {
        lateinit var instance: DuelSystem
    }

    override fun startup() {
        instance = this
        val world = server.getWorld("world") ?: return
        @Suppress("DEPRECATION")
        world.pvp = false
        world.difficulty = Difficulty.PEACEFUL

        //commands
        DuelCommand()
        SetKitCommand()
        GetKitCommand()

        //events
        OnJoin
        SpectateGui
    }

    override fun shutdown() {
        val pluginFolder = dataFolder
        if (pluginFolder.exists() && pluginFolder.isDirectory) {
            pluginFolder.listFiles()?.forEach { file ->
                if (file.isDirectory && file.name.startsWith("duel-") && file.name != "duel-template") {
                    file.deleteRecursively()
                    logger.info("Deleted world (${file.name}).")
                }
            }
        }
    }
}
