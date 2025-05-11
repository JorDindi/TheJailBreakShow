package com.thejailbreakshow.lastrequest;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class LRGame {
    protected final Player prisoner;
    protected final Player guard;
    protected final Map<UUID, BukkitTask> tasks = new HashMap<>();
    protected boolean isActive = false;

    public LRGame(Player prisoner, Player guard) {
        this.prisoner = prisoner;
        this.guard = guard;
    }

    public abstract void start();
    public abstract void end(Player winner);
    public abstract void cancel();

    protected void sendMessage(String message) {
        prisoner.sendMessage(MiniMessage.miniMessage().deserialize(message));
        guard.sendMessage(MiniMessage.miniMessage().deserialize(message));
    }

    protected void cleanup() {
        tasks.values().forEach(BukkitTask::cancel);
        tasks.clear();
        isActive = false;
    }

    public boolean isActive() {
        return isActive;
    }

    public Player getPrisoner() {
        return prisoner;
    }

    public Player getGuard() {
        return guard;
    }
} 