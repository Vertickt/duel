package io.github.vertickt.duelSystem.commands

import io.github.vertickt.duelSystem.DuelSystem
import net.axay.kspigot.commands.command
import net.axay.kspigot.commands.requiresPermission
import net.axay.kspigot.commands.runs
import org.bukkit.inventory.ItemStack

class GetKitCommand {
    val getKit = command("getkit") {
        requiresPermission("duel.command.getkit")
        runs {
            val contentsList = DuelSystem.instance.config.getList("contents")?.map { it as? ItemStack }?.toTypedArray()
            val armorList = DuelSystem.instance.config.getList("armor")?.map { it as? ItemStack }?.toTypedArray()
            if (contentsList != null) player.inventory.contents = contentsList
            if (armorList != null) player.inventory.armorContents = armorList
            player.sendMessage("Kit loaded successfully!")
        }
    }
}