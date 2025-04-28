package com.thejailbreakshow.commands;

import com.thejailbreakshow.economy.EconomyManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GoldCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Players only.");
            return true;
        }

        int gold = EconomyManager.getBalance(player);
        player.sendMessage(MiniMessage.miniMessage().deserialize("<yellow>You have <gold>" + gold + " Gold</gold>."));
        return true;
    }
}
