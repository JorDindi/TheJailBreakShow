package com.thejailbreakshow.lastrequest.games;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayDeque;
import java.util.Deque;

public class Shot4ShotManager {

    private static Player prisoner;
    private static Deque<Player> guards = new ArrayDeque<>();
    private static Player currentShooter;

    public static void start(Player prisonerPlayer, Deque<Player> guardsQueue) {
        prisoner = prisonerPlayer;
        guards = guardsQueue;

        giveDeagle(prisoner);
        currentShooter = prisoner;

        prisoner.sendMessage(MiniMessage.miniMessage().deserialize("<yellow>Your turn! Shoot a Guard!"));
    }

    private static void giveDeagle(Player player) {
        player.getInventory().clear();
        ItemStack crossbow = new ItemStack(Material.CROSSBOW);
        player.getInventory().addItem(crossbow);
        player.getInventory().addItem(new ItemStack(Material.ARROW, 1));
    }

    public static void handleShot(Player shooter) {
        if (!shooter.equals(currentShooter)) {
            shooter.sendMessage(MiniMessage.miniMessage().deserialize("<red>It's not your turn!"));
            return;
        }

        // Switch turns
        if (currentShooter.equals(prisoner)) {
            // Now guard shoots
            currentShooter = guards.peek();
            if (currentShooter != null) {
                giveDeagle(currentShooter);
                currentShooter.sendMessage(MiniMessage.miniMessage().deserialize("<yellow>Your turn! Shoot back!"));
            }
        } else {
            // Prisoner shoots again
            currentShooter = prisoner;
            giveDeagle(prisoner);
            prisoner.sendMessage(MiniMessage.miniMessage().deserialize("<yellow>Your turn!"));
        }
    }

    public static void handleKill(Player killer, Player victim) {
        if (victim.equals(currentShooter)) {
            Bukkit.broadcast(MiniMessage.miniMessage().deserialize("<green><bold>" + killer.getName() + "</bold> wins the Shot4Shot duel!"));
            endGame();
        } else {
            Bukkit.broadcast(MiniMessage.miniMessage().deserialize("<red>No kill yet. Next turn."));
        }
    }

    public static void endGame() {
        prisoner = null;
        guards.clear();
        currentShooter = null;
    }
}
