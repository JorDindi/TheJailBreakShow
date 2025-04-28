package com.thejailbreakshow.managers;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TeamManager {

    public static void init() {

    }

    public static void assignTeams() {
        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers()); // FIXED

        int guardCount = Math.max(1, players.size() / 10);

        List<Player> guards = players.stream().limit(guardCount).toList();

        for (Player guard : guards) {
            PlayerManager.addGuard(guard.getUniqueId());
            guard.sendMessage(MiniMessage.miniMessage().deserialize("<blue><bold>You are a GUARD!"));
        }

        for (Player player : players) {
            if (!guards.contains(player)) {
                PlayerManager.addPrisoner(player.getUniqueId());
                player.sendMessage(MiniMessage.miniMessage().deserialize("<red><bold>You are a PRISONER!"));
            }
        }

        Bukkit.broadcast(MiniMessage.miniMessage().deserialize("<green><bold>Teams have been assigned!"));
    }
}