package com.thejailbreakshow.lastrequest;

import org.bukkit.entity.Player;

public class LastRequestManager {

    private static Player currentChooser = null;

    public static void startLastRequest(Player prisoner) {
        currentChooser = prisoner;
        LastRequestMenu.openMenu(prisoner);
    }

    public static Player getCurrentChooser() {
        return currentChooser;
    }

    public static void endLastRequest() {
        currentChooser = null;
    }
}
