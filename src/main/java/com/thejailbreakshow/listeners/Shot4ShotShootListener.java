package com.thejailbreakshow.listeners;

import com.thejailbreakshow.lastrequest.games.Shot4ShotManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class Shot4ShotShootListener implements Listener {

    @EventHandler
    public void onPlayerShoot(PlayerInteractEvent event) {
        if (event.getItem() == null) return;
        if (event.getItem().getType() != Material.CROSSBOW) return;

        Player player = event.getPlayer();

        // Let Shot4ShotManager handle the turn
        Shot4ShotManager.handleShot(player);
    }
}
