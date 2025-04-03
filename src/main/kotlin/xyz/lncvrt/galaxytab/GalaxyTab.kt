package xyz.lncvrt.galaxytab

import me.clip.placeholderapi.PlaceholderAPI
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import xyz.lncvrt.galaxytab.commands.GalaxyTabCommand
import xyz.lncvrt.galaxytab.events.PlayerJoinEventListener
import java.io.File
import java.io.InputStreamReader
import java.lang.String
import kotlin.Exception
import kotlin.Int
import kotlin.Suppress
import kotlin.let


class GalaxyTab : JavaPlugin(), Listener {
    private val miniMessage = MiniMessage.miniMessage()
    var mainConfig: FileConfiguration? = null
    var usePlaceholderAPI = false
    var placeholderTask: Int? = null

    override fun onEnable() {
        instance = this
        logger.info("Loading plugin")

        //misc
        loadConfig()

        //events
        server.pluginManager.registerEvents(PlayerJoinEventListener(), this)

        //commands
        GalaxyTabCommand()

        if (usePlaceholderAPI) {
            startPlaceholderTask()
        }
    }

    fun loadConfig() {
        if (!dataFolder.exists()) {
            dataFolder.mkdirs()
        }

        val configFile = File(dataFolder, "config.yml")
        if (!configFile.exists()) {
            saveResource(configFile.name, false)
            logger.info("${configFile.name} doesn't exist, creating it...")
        }
        mainConfig = YamlConfiguration()
        mainConfig?.load(configFile)
        updateYamlFile(configFile)

        logger.info("Loaded config!")
        hookPlaceholderAPI()
    }

    fun hookPlaceholderAPI() {
        if (mainConfig?.getBoolean("placeholderapi-hook") == true) {
            if (server.pluginManager.isPluginEnabled("PlaceholderAPI")) {
                usePlaceholderAPI = true
                logger.info("Hooked into PlaceholderAPI!")
            } else {
                logger.warning("Failed to hook into PlaceholderAPI")
            }
        } else {
            usePlaceholderAPI = false
        }
    }

    fun setTab(player: Player) {
        val headerRaw = String.join("\n", mainConfig?.getStringList("header") ?: listOf())
        val footerRaw = String.join("\n", mainConfig?.getStringList("footer") ?: listOf())
        val header: Component = miniMessage.deserialize(if (usePlaceholderAPI) PlaceholderAPI.setPlaceholders(player, headerRaw) else headerRaw)
        val footer: Component = miniMessage.deserialize(if (usePlaceholderAPI) PlaceholderAPI.setPlaceholders(player, footerRaw) else footerRaw)
        player.sendPlayerListHeaderAndFooter(header, footer)
    }

    fun updateYamlFile(file: File) {
        @Suppress("DEPRECATION") val pluginVersion = description.version
        val oldConfig = YamlConfiguration()

        if (file.exists()) {
            try {
                oldConfig.load(file)
            } catch (e: Exception) {
                e.printStackTrace()
                return
            }
        }

        var configVersion = oldConfig.getString("config-version")
        if (configVersion == pluginVersion) return
        val defaultConfigStream = getResource(file.name) ?: return
        val defaultConfig = YamlConfiguration()

        try {
            defaultConfig.load(InputStreamReader(defaultConfigStream))
        } catch (e: Exception) {
            e.printStackTrace()
            return
        }

        for (key in defaultConfig.getKeys(true)) {
            if (oldConfig.contains(key)) {
                defaultConfig.set(key, oldConfig.get(key))
            }
        }

        defaultConfig.set("config-version", pluginVersion)
        defaultConfig.save(file)

        if (file.name == "config.yml") mainConfig = defaultConfig
        if (configVersion == null) configVersion = "n/a (Legacy)"

        logger.info("Updated ${file.name} from $configVersion to $pluginVersion")
    }

    fun startPlaceholderTask() {
        stopTask()
        val delay = mainConfig?.getLong("placeholder-update-delay", 500) ?: 500
        val validDelay = if (delay == 0L) 500L else maxOf(delay / 50, 1)

        placeholderTask = server.scheduler.runTaskTimer(this, Runnable {
            for (player in server.onlinePlayers) {
                setTab(player)
            }
        }, 0L, validDelay).taskId
    }

    fun restartTask() {
        startPlaceholderTask()
    }

    fun stopTask() {
        placeholderTask?.let { server.scheduler.cancelTask(it) }
    }

    companion object {
        private lateinit var instance: GalaxyTab

        fun getInstance(): GalaxyTab {
            return instance
        }
    }
}
