package com.thejailbreakshow.listeners;

import com.thejailbreakshow.gangs.Gang;
import com.thejailbreakshow.gangs.GangManager;
import com.thejailbreakshow.lastrequest.LRManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GangListener implements Listener {

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player victim)) return;
        if (!(event.getDamager() instanceof Player attacker)) return;

        Gang victimGang = GangManager.getGang(victim);
        Gang attackerGang = GangManager.getGang(attacker);

        if (victimGang != null && attackerGang != null && victimGang == attackerGang) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (GangManager.isInGang(player)) {
            // Reapply glow effect when player joins
            GangManager.getGang(player).applyGlow(player);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (GangManager.isInGang(player)) {
            // Remove glow effect when player leaves
            GangManager.removeGlow(player);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();

        if (killer != null) {
            // Handle gang kills
            GangManager.handleKill(killer, victim);
        }

        // Check if this was part of a Last Request game
        if (LRManager.isInGame(victim)) {
            Player opponent = LRManager.getOpponent(victim);
            if (opponent != null) {
                // Handle gang game results
                GangManager.handleGameEnd(opponent, victim);
            }
        }
    }
}
