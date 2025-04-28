package com.thejailbreakshow.listeners;

import com.thejailbreakshow.gangs.Gang;
import com.thejailbreakshow.gangs.GangManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

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
}
