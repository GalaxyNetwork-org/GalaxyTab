package xyz.lncvrt.galaxytab

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin
import java.lang.String

class GalaxyTab : JavaPlugin(), Listener {
    private val miniMessage = MiniMessage.miniMessage()
    private var configFile: FileConfiguration? = null

    override fun onEnable() {
        saveDefaultConfig()
        configFile = config
        server.pluginManager.registerEvents(this, this)
    }

    @EventHandler
    fun onPlayerJoinEvent(event: PlayerJoinEvent) {
        val header: Component = miniMessage.deserialize(String.join("\n", config.getStringList("header")))
        val footer: Component = miniMessage.deserialize(String.join("\n", config.getStringList("footer")))
        event.getPlayer().sendPlayerListHeaderAndFooter(header, footer)
    }
}
