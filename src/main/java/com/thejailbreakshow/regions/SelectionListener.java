package com.thejailbreakshow.regions;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class SelectionListener implements Listener {

    @EventHandler
    public void onBlockClick(PlayerInteractEvent event) {
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE) return;
        if (!event.hasBlock()) return;

        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            SelectionManager.setPos1(event.getPlayer(), event.getClickedBlock().getLocation());
            event.getPlayer().sendMessage(MiniMessage.miniMessage().deserialize("<gray>Set <green>Point 1</green> at <white>" + event.getClickedBlock().getLocation().toVector() + "</white>"));
        }

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            SelectionManager.setPos2(event.getPlayer(), event.getClickedBlock().getLocation());
            event.getPlayer().sendMessage(MiniMessage.miniMessage().deserialize("<gray>Set <green>Point 2</green> at <white>" + event.getClickedBlock().getLocation().toVector() + "</white>"));
        }
    }
}
