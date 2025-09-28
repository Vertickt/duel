package io.github.vertickt.duelSystem.commands

import io.github.vertickt.duelSystem.utils.Duel
import io.github.vertickt.duelSystem.utils.addCommand
import io.github.vertickt.duelSystem.utils.addHover
import io.github.vertickt.duelSystem.utils.cmp
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.commands.argument
import net.axay.kspigot.commands.command
import net.axay.kspigot.commands.literal
import net.axay.kspigot.commands.runs
import net.axay.kspigot.commands.suggestList
import net.axay.kspigot.extensions.onlinePlayers
import net.axay.kspigot.extensions.server
import org.bukkit.entity.Player

class DuelCommand {
    val requests = mutableMapOf<Player, Player>()

    val duelCommand = command("duel") {
        argument<String>("player") {
            suggestList { onlinePlayers.map { it.name } }
            runs {
                val target = getTarget(getArgument<String>("player"), player) ?: return@runs
                if (target == player) {
                        player.sendMessage(cmp("Du kannst dir nicht selber eine Anfrage senden!", KColors.INDIANRED))
                        return@runs
                }

                if (requests[player] == target) {
                    player.sendMessage(cmp("Du hast diesem Spieler bereits eine Duel anfrage gesendet!", KColors.INDIANRED))
                    return@runs
                }

                requests[player] = target
                target.sendMessage(cmp("Du hast eine Duel anfrage von ${player.name} erhalten.", KColors.SKYBLUE)
                    .addCommand("duel ${player.name} accept")
                    .addHover(cmp("accept ${player.name}", KColors.ALICEBLUE)))
                player.sendMessage(cmp("Du hast ${target.name} eine Anfrage gesendet.", KColors.SKYBLUE)
                    .addCommand("duel ${target.name} decline")
                    .addHover(cmp("decline ${target.name}", KColors.ALICEBLUE)))
            }

            literal("accept") {
                runs {
                    val target = getTarget(getArgument<String>("player"), player) ?: return@runs
                    if (requests[target] == player) {
                        requests.remove(target, player)
                        Duel(target, player)
                    } else {
                        player.sendMessage(cmp("Du hast keine Anfrage von diesem Spieler.", KColors.INDIANRED))
                    }
                }
            }
            literal("decline") {
                runs {
                    val target = getTarget(getArgument<String>("player"), player) ?: return@runs
                    if (requests[player] == target) {
                        requests.remove(player, target)
                        player.sendMessage(cmp("Du hast die Anfrage für ${target.name} zurückgezogen", KColors.ALICEBLUE))
                    } else {
                        player.sendMessage(cmp("Du hast diesem Spieler keine Anfrage gesendet!", KColors.INDIANRED))
                    }
                }
            }
        }
    }
    fun getTarget(string: String, player: Player) : Player? {
        val target = server.getPlayer(string)
        if (target == null) {
            player.sendMessage(cmp("Der Spieler ist nicht Online!", KColors.INDIANRED))
            return null
        }
        if (player.world.name.startsWith("duel-")) {
            player.sendMessage(cmp("Du befindest dich in einem Duel!", KColors.INDIANRED))
            return null
        }
        return target
    }
}