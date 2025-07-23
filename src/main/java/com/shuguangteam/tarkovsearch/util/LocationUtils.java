package com.shuguangteam.tarkovsearch.util;

import org.bukkit.Location;

public class LocationUtils {

    public static String key(Location loc) {
        return loc.getWorld().getName() + "," + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ();
    }
}
