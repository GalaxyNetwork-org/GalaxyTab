package xyz.lncvrt.galaxytab.commands

import net.kyori.adventure.text.minimessage.MiniMessage
import org.incendo.cloud.kotlin.MutableCommandBuilder
import xyz.lncvrt.galaxytab.GalaxyTab
import xyz.lncvrt.galaxytab.commands.api.BukkitCommand
import xyz.lncvrt.galaxytab.commands.api.BukkitSender

class GalaxyTabCommand : BukkitCommand(GalaxyTab.getInstance(), "galaxytab", arrayOf("tab")) {
    override fun rootBuilder(builder: MutableCommandBuilder<BukkitSender>) {
        builder.handler { context ->
            val sender = context.sender().platformSender()
            val miniMessage = MiniMessage.miniMessage()

            sender.sendMessage(miniMessage.deserialize("<rainbow>GalaxyTab</rainbow> <light_purple>by Lncvrt</light_purple>"))
        }
    }

    init {
        rawCommandBuilder().registerCopy {
            literal("reload")
            permission("${GalaxyTab.getInstance().name.lowercase()}.reload")
            handler { context ->
                val sender = context.sender().platformSender()
                val miniMessage = MiniMessage.miniMessage()
                val instance = GalaxyTab.getInstance()

                instance.saveDefaultConfig()
                instance.reloadConfig()
                instance.configFile = instance.config
                for (player in instance.server.onlinePlayers) instance.setTab(player)

                sender.sendMessage(miniMessage.deserialize("<rainbow>GalaxyTab</rainbow> <light_purple>has been reloaded!</light_purple>"))
            }
        }
    }
}
