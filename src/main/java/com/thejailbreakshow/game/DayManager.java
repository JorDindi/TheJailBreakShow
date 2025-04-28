package com.thejailbreakshow.game;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class DayManager {

    private static DayState currentDay = DayState.DAY_1;

    public static void startDays() {
        currentDay = DayState.DAY_1;
        broadcastDay();
    }

    public static void nextDay() {
        switch (currentDay) {
            case DAY_1 -> currentDay = DayState.DAY_2;
            case DAY_2 -> currentDay = DayState.DAY_3;
            case DAY_3 -> currentDay = DayState.DAY_4;
            case DAY_4 -> currentDay = DayState.DAY_5;
            case DAY_5 -> {
                currentDay = DayState.FREE_DAY;
                FreeDayManager.startFreeDay();
            }
            case FREE_DAY -> {
                currentDay = DayState.ELECTION_DAY;
                ElectionManager.startElection();
            }
            case ELECTION_DAY -> {
                currentDay = DayState.DAY_1;
                broadcastDay();
            }
        }

        if (currentDay != DayState.FREE_DAY && currentDay != DayState.ELECTION_DAY) {
            broadcastDay();
        }
    }

    private static void broadcastDay() {
        Bukkit.broadcast(MiniMessage.miniMessage().deserialize("<blue><bold>Starting " + currentDay.name().replace("_", " ") + "!"));
    }

    public static DayState getCurrentDay() {
        return currentDay;
    }
}
