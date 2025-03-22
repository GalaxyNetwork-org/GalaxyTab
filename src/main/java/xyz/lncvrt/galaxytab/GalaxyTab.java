package xyz.lncvrt.galaxytab;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class GalaxyTab extends JavaPlugin implements Listener {
    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    private FileConfiguration config;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        config = getConfig();
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        Component header = miniMessage.deserialize(String.join("\n", config.getStringList("header")));
        Component footer = miniMessage.deserialize(String.join("\n", config.getStringList("footer")));
        event.getPlayer().sendPlayerListHeaderAndFooter(header, footer);
    }
}
