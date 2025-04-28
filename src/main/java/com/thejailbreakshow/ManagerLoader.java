package com.thejailbreakshow;

import com.thejailbreakshow.commands.CommandManager;
import com.thejailbreakshow.game.DayManager;
import com.thejailbreakshow.game.GameManager;
import com.thejailbreakshow.listeners.ListenerManager;
import com.thejailbreakshow.managers.PlayerManager;
import com.thejailbreakshow.managers.TeamManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ManagerLoader {

    public static void init(JavaPlugin plugin) {
        PlayerManager.init();
        TeamManager.init();
        GameManager.init();
        ListenerManager.registerListeners(plugin);
        CommandManager.registerCommands(plugin);
        GameManager.init();
        DayManager.startDays();
    }
}
