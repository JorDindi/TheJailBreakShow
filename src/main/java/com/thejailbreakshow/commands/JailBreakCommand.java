package com.thejailbreakshow.commands;

import com.thejailbreakshow.game.GameManager;
import com.thejailbreakshow.managers.TeamManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JailBreakCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can run this command.");
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("debug")) {
            TeamManager.assignTeams();
            GameManager.startGame();
            player.sendMessage(MiniMessage.miniMessage().deserialize("<gold><bold>[DEBUG]</bold> Game started and teams assigned!"));
            return true;
        }

        player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Usage: /jb debug"));
        return true;
    }
}
