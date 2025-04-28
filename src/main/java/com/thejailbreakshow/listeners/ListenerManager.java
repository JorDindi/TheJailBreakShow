package com.thejailbreakshow.listeners;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ListenerManager {

    public static void registerListeners(JavaPlugin plugin) {
        PluginManager pm = plugin.getServer().getPluginManager();
        pm.registerEvents(new PlayerJoinListener(), plugin);
        pm.registerEvents(new LeaderChallengeListener(), plugin);
        pm.registerEvents(new CellButtonListener(), plugin);
        pm.registerEvents(new GangListener(), plugin);
        pm.registerEvents(new LastRequestTriggerListener(), plugin);
        pm.registerEvents(new Shot4ShotShootListener(), plugin);
        pm.registerEvents(new Shot4ShotKillListener(), plugin);
    }
}
