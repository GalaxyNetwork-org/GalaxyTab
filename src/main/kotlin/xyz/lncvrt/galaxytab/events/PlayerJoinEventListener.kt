package xyz.lncvrt.galaxytab.events

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import xyz.lncvrt.galaxytab.GalaxyTab

class PlayerJoinEventListener : Listener {
    @EventHandler
    fun onPlayerJoinEvent(event: PlayerJoinEvent) {
        GalaxyTab.getInstance().setTab(event.player)
    }
}