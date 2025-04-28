package com.thejailbreakshow.cells;

import org.bukkit.Location;

public class CellButtonManager {

    private static Location cellButtonLocation;

    public static void setCellButtonLocation(Location location) {
        cellButtonLocation = location;
    }

    public static Location getCellButtonLocation() {
        return cellButtonLocation;
    }

    public static boolean isCellButton(Location location) {
        if (cellButtonLocation == null) return false;
        return location.equals(cellButtonLocation);
    }
}
