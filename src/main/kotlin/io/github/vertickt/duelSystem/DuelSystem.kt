package io.github.vertickt.duelSystem

import io.github.vertickt.duelSystem.commands.DuelCommand
import io.github.vertickt.duelSystem.listeners.OnJoin
import net.axay.kspigot.main.KSpigot
import org.bukkit.Difficulty


class DuelSystem : KSpigot() {

    override fun startup() {
        val world = server.getWorld("world") ?: return
        @Suppress("DEPRECATION")
        world.pvp = false
        world.difficulty = Difficulty.PEACEFUL

        //commands
        DuelCommand()

        //events
        OnJoin
    }

    override fun shutdown() {
        // Plugin shutdown logic
    }
}
