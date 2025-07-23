package com.shuguangteam.tarkovsearch.util;

import com.shuguangteam.tarkovsearch.TarkovSearch;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public class SchedulerUtils {
    private static final TarkovSearch plugin = TarkovSearch.getInstance();

    public static BukkitTask runLater(Runnable r, long delayTick) {
        return Bukkit.getScheduler().runTaskLater(plugin, r, delayTick);
    }

    public static BukkitTask runTimer(Runnable r, long delay, long period) {
        return Bukkit.getScheduler().runTaskTimer(plugin, r, delay, period);
    }
}
