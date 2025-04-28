package com.thejailbreakshow.listeners;

import com.thejailbreakshow.cells.CellButtonManager;
import com.thejailbreakshow.cells.CellManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class CellButtonListener implements Listener {

    @EventHandler
    public void onButtonPress(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) return;
        if (event.getClickedBlock().getType() != Material.STONE_BUTTON && event.getClickedBlock().getType() != Material.OAK_BUTTON) return;
        if (event.getAction() != org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK) return;

        Block block = event.getClickedBlock();
        if (!CellButtonManager.isCellButton(block.getLocation())) return; // ✅ Check if correct button

        Player player = event.getPlayer();
        CellManager.openAllCells();
        player.sendMessage(MiniMessage.miniMessage().deserialize("<green>You opened the cells!"));
    }
}
