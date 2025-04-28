package com.thejailbreakshow.listeners;

import com.thejailbreakshow.lastrequest.games.Shot4ShotManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class Shot4ShotKillListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (event.getEntity().getKiller() == null) return;
        if (!(event.getEntity() instanceof Player victim)) return;
        Player killer = event.getEntity().getKiller();

        // Let Shot4ShotManager handle the kill
        Shot4ShotManager.handleKill(killer, victim);
    }
}
