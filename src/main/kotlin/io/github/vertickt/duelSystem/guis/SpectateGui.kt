package io.github.vertickt.duelSystem.guis

import io.github.vertickt.duelSystem.utils.cmp
import io.github.vertickt.duelSystem.utils.spectateItem
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.server
import net.axay.kspigot.gui.GUIType
import net.axay.kspigot.gui.Slots
import net.axay.kspigot.gui.kSpigotGUI
import net.axay.kspigot.gui.openGUI
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

object SpectateGui {
    val onClick = listen<PlayerInteractEvent> {
        if (it.action != Action.RIGHT_CLICK_AIR && it.action != Action.RIGHT_CLICK_BLOCK) return@listen
        if (it.item?.isSimilar(spectateItem) != true) return@listen
        val player = it.player
        spectateGUI(player)
    }

    fun spectateGUI(player: Player) {
        val ui = server.createInventory(null, 5*9, cmp("Spectate", NamedTextColor.GRAY))

        player.openInventory(ui)
    }
}