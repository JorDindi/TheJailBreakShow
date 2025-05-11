package com.thejailbreakshow.lastrequest;

import com.thejailbreakshow.TheJailBreakShow;
import com.thejailbreakshow.lastrequest.games.BoxingMatch;
import com.thejailbreakshow.lastrequest.games.KnifeFight;
import com.thejailbreakshow.lastrequest.games.Shot4ShotManager;
import com.thejailbreakshow.lastrequest.games.GrenadeToss;
import com.thejailbreakshow.lastrequest.games.HeadshotOnlyAWP;
import com.thejailbreakshow.lastrequest.games.HeadshotOnlyKnife;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LRManager {
    private static final Map<UUID, LRGame> activeGames = new HashMap<>();

    public static void init() {
        // Initialize any necessary components
    }

    public static boolean startKnifeFight(Player prisoner, Player guard) {
        // Check if either player is already in a game
        if (activeGames.containsKey(prisoner.getUniqueId()) || activeGames.containsKey(guard.getUniqueId())) {
            prisoner.sendMessage(MiniMessage.miniMessage().deserialize("<red>One or both players are already in a Last Request game!"));
            return false;
        }

        // Start the game using KnifeFight from games directory
        KnifeFight.start(prisoner, guard);
        return true;
    }

    public static boolean startShot4Shot(Player prisoner, Player guard) {
        // Check if either player is already in a game
        if (activeGames.containsKey(prisoner.getUniqueId()) || activeGames.containsKey(guard.getUniqueId())) {
            prisoner.sendMessage(MiniMessage.miniMessage().deserialize("<red>One or both players are already in a Last Request game!"));
            return false;
        }

        // Create guard queue
        Deque<Player> guards = new ArrayDeque<>();
        guards.add(guard);

        // Start the game using existing Shot4ShotManager
        Shot4ShotManager.start(prisoner, guards);
        return true;
    }

    public static boolean startBoxingMatch(Player prisoner, Player guard) {
        // Check if either player is already in a game
        if (activeGames.containsKey(prisoner.getUniqueId()) || activeGames.containsKey(guard.getUniqueId())) {
            prisoner.sendMessage(MiniMessage.miniMessage().deserialize("<red>One or both players are already in a Last Request game!"));
            return false;
        }

        // Start the game using BoxingMatch
        BoxingMatch.start(prisoner, guard);
        return true;
    }

    public static void endGame(LRGame game) {
        if (game == null) return;

        activeGames.remove(game.getPrisoner().getUniqueId());
        activeGames.remove(game.getGuard().getUniqueId());
        game.cancel();
    }

    public static boolean isInGame(Player player) {
        return activeGames.containsKey(player.getUniqueId());
    }

    public static LRGame getGame(Player player) {
        return activeGames.get(player.getUniqueId());
    }

    public static boolean startGrenadeToss(Player prisoner, Player guard) {
        // Check if either player is already in a game
        if (activeGames.containsKey(prisoner.getUniqueId()) || activeGames.containsKey(guard.getUniqueId())) {
            prisoner.sendMessage(MiniMessage.miniMessage().deserialize("<red>One or both players are already in a Last Request game!"));
            return false;
        }
        GrenadeToss.start(prisoner, guard);
        return true;
    }

    public static boolean startHeadshotOnlyAWP(Player prisoner, Player guard) {
        if (activeGames.containsKey(prisoner.getUniqueId()) || activeGames.containsKey(guard.getUniqueId())) {
            prisoner.sendMessage(MiniMessage.miniMessage().deserialize("<red>One or both players are already in a Last Request game!"));
            return false;
        }
        HeadshotOnlyAWP.start(prisoner, guard);
        return true;
    }

    public static boolean startHeadshotOnlyKnife(Player prisoner, Player guard) {
        if (activeGames.containsKey(prisoner.getUniqueId()) || activeGames.containsKey(guard.getUniqueId())) {
            prisoner.sendMessage(MiniMessage.miniMessage().deserialize("<red>One or both players are already in a Last Request game!"));
            return false;
        }
        HeadshotOnlyKnife.start(prisoner, guard);
        return true;
    }

    public static Player getOpponent(Player player) {
        LRGame game = activeGames.get(player.getUniqueId());
        if (game == null) return null;
        
        // Get the other player in the game
        for (UUID playerId : activeGames.keySet()) {
            if (!playerId.equals(player.getUniqueId())) {
                return Bukkit.getPlayer(playerId);
            }
        }
        return null;
    }
} 