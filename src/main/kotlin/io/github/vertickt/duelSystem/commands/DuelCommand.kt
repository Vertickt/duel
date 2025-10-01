package io.github.vertickt.duelSystem.commands

import io.github.vertickt.duelSystem.utils.Duel
import io.github.vertickt.duelSystem.utils.addCommand
import io.github.vertickt.duelSystem.utils.addHover
import io.github.vertickt.duelSystem.utils.cmp
import io.github.vertickt.duelSystem.utils.plus
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.commands.argument
import net.axay.kspigot.commands.command
import net.axay.kspigot.commands.literal
import net.axay.kspigot.commands.runs
import net.axay.kspigot.commands.suggestList
import net.axay.kspigot.extensions.onlinePlayers
import net.axay.kspigot.extensions.server
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.entity.Player

class DuelCommand {
    val requests = mutableMapOf<Player, MutableSet<Player>>()

    val duelCommand = command("duel") {
        argument<String>("player") {
            suggestList { onlinePlayers.map { it.name } }
            runs {
                val target = getTarget(getArgument<String>("player"), player) ?: return@runs
                if (target == player) {
                    player.sendMessage(cmp("You cannot send a request to yourself!", KColors.INDIANRED))
                    return@runs
                }
                if (requests[player]?.contains(target) == true) {
                    player.sendMessage(cmp("You have already sent a duel request to this player!", KColors.INDIANRED))
                    return@runs
                }
                requests.computeIfAbsent(player) { mutableSetOf() }.add(target)
                target.sendMessage(
                    cmp("You have received a duel request from ${player.name}") +
                            cmp(" [Accept]", NamedTextColor.GREEN)
                                .addCommand("duel ${player.name} accept")
                                .addHover(cmp("Click to accept the request from ${player.name}"))
                )
                player.sendMessage(
                    cmp("You have sent a request to ${target.name}") +
                            cmp(" [Cancel]", NamedTextColor.RED)
                                .addCommand("duel ${target.name} decline")
                                .addHover(cmp("Click to cancel the request to ${target.name}"))
                )
            }

            literal("accept") {
                runs {
                    val target = getTarget(getArgument<String>("player"), player) ?: return@runs
                    if (target.world.name.startsWith("duel-")) {
                        player.sendMessage(cmp("This Player cannot be challenged currently", KColors.INDIANRED))
                        return@runs
                    }
                    if (requests[target]?.contains(player) == true) {
                        requests[target]?.remove(player)
                        Duel(target, player)
                    } else {
                        player.sendMessage(cmp("You don’t have a request from this player.", KColors.INDIANRED))
                    }
                }
            }
            literal("decline") {
                runs {
                    val target = getTarget(getArgument<String>("player"), player) ?: return@runs
                    if (requests[player] == target) {
                        requests.remove(player, target)
                        player.sendMessage(cmp("You have withdrawn the request for ${target.name}", KColors.ALICEBLUE))
                    } else {
                        player.sendMessage(cmp("You haven’t sent a request to this player!", KColors.INDIANRED))
                    }
                }
            }
        }
    }
    fun getTarget(string: String, player: Player) : Player? {
        val target = server.getPlayer(string)
        if (target == null) {
            player.sendMessage(cmp("That player is not online!", KColors.INDIANRED))
            return null
        }
        if (player.world.name.startsWith("duel-")) {
            player.sendMessage(cmp("You are currently in a duel!", KColors.INDIANRED))
            return null
        }
        return target
    }
}
