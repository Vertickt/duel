package io.github.vertickt.duelSystem.guis

import io.github.vertickt.duelSystem.utils.spectateItem
import net.axay.kspigot.event.listen
import net.axay.kspigot.gui.GUIType
import net.axay.kspigot.gui.Slots
import net.axay.kspigot.gui.kSpigotGUI
import net.axay.kspigot.gui.openGUI
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

object SpectateGui {
    val onClick = listen<PlayerInteractEvent> {
        if (it.action != Action.RIGHT_CLICK_AIR && it.action != Action.RIGHT_CLICK_BLOCK) return@listen
        if (it.item?.isSimilar(spectateItem) != true) return@listen
        val player = it.player
        player.openGUI(spectateGui)
    }

    val spectateGui = kSpigotGUI(GUIType.FIVE_BY_NINE) {
        page(1) {
            val compound = createSimpleRectCompound(
                Slots.RowOneSlotOne, Slots.RowFiveSlotNine,
            )
        }
    }
}