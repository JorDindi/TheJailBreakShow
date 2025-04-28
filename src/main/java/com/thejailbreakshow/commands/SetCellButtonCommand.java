package com.thejailbreakshow.commands;

import com.thejailbreakshow.cells.CellButtonManager;
import com.thejailbreakshow.cells.CellButtonSaveManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class SetCellButtonCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Players only.");
            return true;
        }

        Block target = player.getTargetBlockExact(5);
        if (target == null || (target.getType() != Material.STONE_BUTTON && target.getType() != Material.OAK_BUTTON)) {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>You must look at a button!"));
            return true;
        }

        CellButtonManager.setCellButtonLocation(target.getLocation());
        CellButtonSaveManager.save();
        player.sendMessage(MiniMessage.miniMessage().deserialize("<green>Cell button location set!"));
        return true;
    }
}
