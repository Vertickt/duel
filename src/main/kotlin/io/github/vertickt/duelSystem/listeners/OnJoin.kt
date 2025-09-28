package io.github.vertickt.duelSystem.listeners

import io.github.vertickt.duelSystem.utils.cmp
import io.github.vertickt.duelSystem.utils.giveLobbyItems
import io.github.vertickt.duelSystem.utils.resetPlayer
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.server
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.event.player.PlayerJoinEvent

object OnJoin {
    val onJoin = listen<PlayerJoinEvent> {
        val player = it.player
        player.teleportAsync(Location(server.getWorld("world"), 0.5, 100.0, 0.5, 0f, 0f))
        resetPlayer(player)
        player.gameMode = GameMode.ADVENTURE
        giveLobbyItems(player)
    }
}