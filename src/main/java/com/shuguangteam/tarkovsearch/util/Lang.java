package com.shuguangteam.tarkovsearch.util;

import com.shuguangteam.tarkovsearch.TarkovSearch;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

/**
 * 简易多语言工具
 */
public class Lang {

    private final TarkovSearch plugin;
    private FileConfiguration lang;

    public Lang(TarkovSearch plugin) {
        this.plugin = plugin;
        reload();
    }

    public void reload() {
        File file = new File(plugin.getDataFolder(), "messages.yml");
        lang = YamlConfiguration.loadConfiguration(file);
    }

    public String get(String path) {
        return Text.color(lang.getString(path, "§cMissing message: " + path));
    }

    public List<String> getList(String path) {
        List<String> list = lang.getStringList(path);
        list.replaceAll(Text::color);
        return list;
    }

    /** 带前缀消息 */
    public String msg(String path) {
        return get("prefix") + get(path);
    }
}
