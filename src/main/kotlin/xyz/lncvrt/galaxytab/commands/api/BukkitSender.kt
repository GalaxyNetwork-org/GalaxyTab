package xyz.lncvrt.galaxytab.commands.api

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

open class BukkitSender(
    private var commandSender: CommandSender,
) {
    private var onlinePlayersMessage = Component.text("Only players can use this command.")
        .color(TextColor.color(192, 32, 16))
        .asComponent()

    fun player(): Player? {
        if (commandSender is Player) return (commandSender as Player)

        if (onlinePlayersMessage != Component.empty()) {
            commandSender.sendMessage(onlinePlayersMessage)
        }

        return null
    }

    fun audience(): Audience {
        return commandSender
    }

    fun platformSender(): CommandSender {
        return commandSender
    }

    fun platformSender(commandSender: CommandSender) {
        this.commandSender = commandSender
    }

    fun onlyPlayersComponent(onlinePlayersMessage: Component) {
        this.onlinePlayersMessage = onlinePlayersMessage
    }
}