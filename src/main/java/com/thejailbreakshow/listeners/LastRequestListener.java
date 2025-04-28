package com.thejailbreakshow.listeners;

import com.thejailbreakshow.lastrequest.LastRequestManager;
import com.thejailbreakshow.lastrequest.LastRequestType;
import com.thejailbreakshow.lastrequest.games.Shot4ShotManager;
import com.thejailbreakshow.managers.PlayerManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayDeque;
import java.util.Deque;

public class LastRequestListener implements Listener {

    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (!event.getView().getTitle().equals("Choose Last Request")) return;

        event.setCancelled(true);

        Material clicked = event.getCurrentItem() != null ? event.getCurrentItem().getType() : null;
        if (clicked == null) return;

        if (!player.equals(LastRequestManager.getCurrentChooser())) {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>You are not the Last Request chooser!</red>"));
            player.closeInventory();
            return;
        }

        for (LastRequestType type : LastRequestType.values()) {
            if (type.getIcon() == clicked) {
                player.sendMessage(MiniMessage.miniMessage().deserialize("<green>You selected: <white>" + type.getDisplayName() + "</white></green>"));
                player.closeInventory();
                LastRequestManager.endLastRequest();

                if (type == LastRequestType.SHOT4SHOT) {
                    // Build Guards Deque
                    Deque<Player> guards = new ArrayDeque<>();
                    for (Player online : player.getWorld().getPlayers()) {
                        if (PlayerManager.isGuard(online.getUniqueId())) {
                            guards.add(online);
                        }
                    }

                    if (guards.isEmpty()) {
                        player.sendMessage(MiniMessage.miniMessage().deserialize("<red>No guards available to fight!"));
                        return;
                    }

                    Shot4ShotManager.start(player, guards);
                }

                return;
            }
        }

    }
}
