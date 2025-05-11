package com.thejailbreakshow.gangs;

import org.bukkit.Color;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Gang {

    private final String name;
    private final Set<UUID> members = new HashSet<>();
    private final Color color;
    private int kills = 0;
    private int deaths = 0;
    private int wins = 0;
    private int losses = 0;

    public Gang(String name, Player owner, Color color) {
        this.name = name;
        this.color = color;
        addMember(owner);
    }

    public void addMember(Player player) {
        members.add(player.getUniqueId());
    }

    public void removeMember(Player player) {
        members.remove(player.getUniqueId());
    }

    public boolean isMember(Player player) {
        return members.contains(player.getUniqueId());
    }

    public String getName() {
        return name;
    }

    public Set<UUID> getMembers() {
        return members;
    }

    public Color getColor() {
        return color;
    }

    public void addKill() {
        kills++;
    }

    public void addDeath() {
        deaths++;
    }

    public void addWin() {
        wins++;
    }

    public void addLoss() {
        losses++;
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public double getKDR() {
        return deaths == 0 ? kills : (double) kills / deaths;
    }

    public double getWinRate() {
        int total = wins + losses;
        return total == 0 ? 0 : (double) wins / total;
    }

    public void applyGlow(Player player) {
        GangManager.applyGlow(player, color);
    }
}
