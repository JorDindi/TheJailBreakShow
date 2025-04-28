package com.thejailbreakshow.listeners;

import com.thejailbreakshow.economy.EconomyManager;
import com.thejailbreakshow.managers.PlayerManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class KillRewardListener implements Listener {

    @EventHandler
    public void onKill(EntityDeathEvent event) {
        if (!(event.getEntity() instanceof Player victim)) return;
        if (!(event.getEntity().getKiller() instanceof Player killer)) return;

        if (PlayerManager.isGuard(victim.getUniqueId()) && PlayerManager.isPrisoner(killer.getUniqueId())) {
            EconomyManager.addGold(killer, 100);
            killer.sendMessage(MiniMessage.miniMessage().deserialize("<green>You earned <gold>+100 Gold</gold> for killing a Guard!"));
        }
    }
}
