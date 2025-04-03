package xyz.lncvrt.galaxytab

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin
import xyz.lncvrt.galaxytab.commands.GalaxyTabCommand
import java.lang.String

class GalaxyTab : JavaPlugin(), Listener {
    private val miniMessage = MiniMessage.miniMessage()
    var configFile: FileConfiguration? = null

    override fun onEnable() {
        instance = this
        saveDefaultConfig()
        configFile = config
        server.pluginManager.registerEvents(this, this)
        GalaxyTabCommand()
    }

    @EventHandler
    fun onPlayerJoinEvent(event: PlayerJoinEvent) {
        setTab(event.player)
    }

    fun setTab(player: Player) {
        val header: Component = miniMessage.deserialize(String.join("\n", configFile?.getStringList("header")))
        val footer: Component = miniMessage.deserialize(String.join("\n", configFile?.getStringList("footer")))
        player.sendPlayerListHeaderAndFooter(header, footer)
    }

    companion object {
        private lateinit var instance: GalaxyTab

        fun getInstance(): GalaxyTab {
            return instance
        }
    }
}
