package com.thejailbreakshow.commands;

import com.thejailbreakshow.regions.Region;
import com.thejailbreakshow.regions.RegionManager;
import com.thejailbreakshow.regions.RegionSaveManager;
import com.thejailbreakshow.regions.RegionType;
import com.thejailbreakshow.regions.SelectionManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RegionCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Players only.");
            return true;
        }

        if (args.length != 2) {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Usage: /jb setregion <name> <type>"));
            return true;
        }

        String name = args[0];
        String typeName = args[1].toUpperCase();

        RegionType type;
        try {
            type = RegionType.valueOf(typeName);
        } catch (IllegalArgumentException e) {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Invalid RegionType."));
            return true;
        }

        if (!SelectionManager.hasBothPositions(player)) {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>You must select two points first!"));
            return true;
        }

        Region region = new Region(name, type, SelectionManager.getPos1(player), SelectionManager.getPos2(player));
        RegionManager.addRegion(region);
        RegionSaveManager.saveAllRegions();
        SelectionManager.clear(player);

        player.sendMessage(MiniMessage.miniMessage().deserialize("<green>Region <white>" + name + "</white> saved as <yellow>" + typeName + "</yellow>!"));
        return true;
    }
}
