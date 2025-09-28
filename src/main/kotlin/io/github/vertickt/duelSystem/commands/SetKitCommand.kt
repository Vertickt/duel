package io.github.vertickt.duelSystem.commands

import io.github.vertickt.duelSystem.DuelSystem
import io.github.vertickt.duelSystem.utils.KitStorage
import net.axay.kspigot.commands.command
import net.axay.kspigot.commands.requiresPermission
import net.axay.kspigot.commands.runs

class SetKitCommand {
    val setKitCommand = command("setkit") {
        requiresPermission("duel.command.setkit")
        runs {
            val plugin = DuelSystem.instance

            val contentsList = player.inventory.contents.toList()
            val armorList = player.inventory.armorContents.toList()

            plugin.config["contents"] = contentsList
            plugin.config["armor"] = armorList
            plugin.saveConfig()

            player.sendMessage("Kit saved successfully!")
        }
    }
}