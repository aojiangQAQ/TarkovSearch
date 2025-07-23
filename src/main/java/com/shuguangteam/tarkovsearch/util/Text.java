package com.shuguangteam.tarkovsearch.util;

import org.bukkit.ChatColor;

/** 颜色代码工具 (& → §) */
public final class Text {
    private Text() {}

    /** 把 &c 等替换为 '§c' */
    public static String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
