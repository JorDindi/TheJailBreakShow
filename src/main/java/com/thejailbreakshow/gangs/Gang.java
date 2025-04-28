package com.thejailbreakshow.gangs;

import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Gang {

    private final String name;
    private final Set<UUID> members = new HashSet<>();

    public Gang(String name, Player owner) {
        this.name = name;
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
}
