package com.thejailbreakshow.listeners;

import com.thejailbreakshow.lastrequest.LastRequestManager;
import com.thejailbreakshow.managers.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.stream.Collectors;

public class LastRequestTriggerListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player died = event.getEntity();

        if (!PlayerManager.isPrisoner(died.getUniqueId())) {
            return; // Only care about Prisoners
        }

        long alivePrisoners = died.getWorld().getPlayers().stream()
                .filter(p -> PlayerManager.isPrisoner(p.getUniqueId()))
                .filter(p -> !p.isDead())
                .count();

        if (alivePrisoners == 1) {
            Player lastPrisoner = died.getWorld().getPlayers().stream()
                    .filter(p -> PlayerManager.isPrisoner(p.getUniqueId()))
                    .filter(p -> !p.isDead())
                    .findFirst()
                    .orElse(null);

            if (lastPrisoner != null) {
                LastRequestManager.startLastRequest(lastPrisoner);
            }
        }
    }
}
