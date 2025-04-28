package com.thejailbreakshow.commands;

import com.thejailbreakshow.gangs.GangManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GangCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players.");
            return true;
        }

        if (args.length < 2) {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Usage: /gang create <name>"));
            return true;
        }

        if (args[0].equalsIgnoreCase("create")) {
            if (GangManager.isInGang(player)) {
                player.sendMessage(MiniMessage.miniMessage().deserialize("<red>You are already in a gang!"));
                return true;
            }

            String name = args[1];
            GangManager.createGang(player, name);
            player.sendMessage(MiniMessage.miniMessage().deserialize("<green>You created a gang named <white>" + name + "</white>!"));
            return true;
        }

        player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Usage: /gang create <name>"));
        return true;
    }
}
