package com.thejailbreakshow.game;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;

public class ElectionManager {

    public static void startElection() {
        Bukkit.broadcast(MiniMessage.miniMessage().deserialize("<gold><bold>Election Day has started!</bold>"));
        LeaderChallengeManager.startChallenge();
    }
}
