package com.thejailbreakshow.managers;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PlayerManager {

    private static final Set<UUID> prisoners = new HashSet<>();
    private static final Set<UUID> guards = new HashSet<>();

    public static void init() {
        prisoners.clear();
        guards.clear();
    }

    public static void addPrisoner(UUID uuid) {
        prisoners.add(uuid);
    }

    public static void addGuard(UUID uuid) {
        guards.add(uuid);
    }

    public static boolean isPrisoner(UUID uuid) {
        return prisoners.contains(uuid);
    }

    public static boolean isGuard(UUID uuid) {
        return guards.contains(uuid);
    }

    public static void reset() {
        prisoners.clear();
        guards.clear();
    }
}
