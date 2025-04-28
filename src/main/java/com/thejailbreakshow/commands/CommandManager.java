package com.thejailbreakshow.commands;

import org.bukkit.plugin.java.JavaPlugin;

public class CommandManager {

    public static void registerCommands(JavaPlugin plugin) {
        plugin.getCommand("jb").setExecutor(new JailBreakCommand());
        plugin.getCommand("jb_setregion").setExecutor(new RegionCommand());
        plugin.getCommand("setcellbutton").setExecutor(new SetCellButtonCommand());
        plugin.getCommand("gang").setExecutor(new GangCommand());
        plugin.getCommand("gold").setExecutor(new GoldCommand());
    }
}
