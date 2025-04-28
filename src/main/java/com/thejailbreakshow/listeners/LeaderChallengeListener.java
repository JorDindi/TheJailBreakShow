package com.thejailbreakshow.listeners;

import com.thejailbreakshow.game.LeaderChallengeManager;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class LeaderChallengeListener implements Listener {

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        String message = PlainTextComponentSerializer.plainText().serialize(event.message());
        LeaderChallengeManager.handleChat(event.getPlayer(), message);
    }
}
