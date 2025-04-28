package com.thejailbreakshow.listeners;

import com.thejailbreakshow.economy.EconomyManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        EconomyManager.initPlayer(event.getPlayer());
        event.getPlayer().sendMessage(MiniMessage.miniMessage().deserialize("<green>Welcome to <bold>The JailBreak Show</bold> server!"));
    }
}
