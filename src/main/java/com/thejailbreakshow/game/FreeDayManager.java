package com.thejailbreakshow.game;

import com.thejailbreakshow.cells.CellManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class FreeDayManager {

    public static void startFreeDay() {
        Bukkit.broadcast(MiniMessage.miniMessage().deserialize("<green><bold>FREE DAY has started!</bold>"));

        // Auto-open all cells
        CellManager.openAllCells();

        // Start countdown
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.broadcast(MiniMessage.miniMessage().deserialize("<red>FreeDay Countdown Over. Start Fighting!"));
                // TODO: Allow prisoners to fight after countdown
            }
        }.runTaskLater(com.thejailbreakshow.TheJailBreakShow.getInstance(), 60 * 20); // 60 seconds
    }
}
