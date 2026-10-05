package com.shuguangteam.tarkovsearch;

import com.shuguangteam.tarkovsearch.command.TsCommandExecutor;
import com.shuguangteam.tarkovsearch.command.TabCompleteTs;
import com.shuguangteam.tarkovsearch.listener.BlockListener;
import com.shuguangteam.tarkovsearch.listener.InventoryListener;
import com.shuguangteam.tarkovsearch.listener.PlayerInteractListener;
import com.shuguangteam.tarkovsearch.manager.BoxManager;
import com.shuguangteam.tarkovsearch.manager.SearchSessionManager;
import com.shuguangteam.tarkovsearch.util.Lang;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

/**
 * 插件主类：TarkovSearch（游戏内显示“搜索”）
 */
public final class TarkovSearch extends JavaPlugin {

    /* ------------------ 单例与组件 ------------------ */

    private static TarkovSearch instance;

    private Lang                 lang;
    private BoxManager           boxManager;
    private SearchSessionManager searchSessionManager;

    /* ------------------ 生命周期 ------------------ */

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        saveResource("messages.yml", false);

        lang                 = new Lang(this);
        boxManager           = new BoxManager(this);
        searchSessionManager = new SearchSessionManager(this);

        TsCommandExecutor cmd = new TsCommandExecutor(this);
        Objects.requireNonNull(getCommand("ts")).setExecutor(cmd);
        Objects.requireNonNull(getCommand("ts")).setTabCompleter(new TabCompleteTs());

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerInteractListener(this), this);
        pm.registerEvents(new InventoryListener(this), this);
        pm.registerEvents(new BlockListener(this), this);

        getLogger().info("[TarkovSearch] 已启用 - 版本 " + getDescription().getVersion());
    }

    @Override
    public void onDisable() {
        if (boxManager != null) boxManager.saveBoxes();
        getLogger().info("[TarkovSearch] 已卸载");
    }

    /* ------------------ Getter ------------------ */

    public static TarkovSearch getInstance()              { return instance; }
    public Lang getLang()                                 { return lang; }
    public BoxManager getBoxManager()                     { return boxManager; }
    public SearchSessionManager getSearchSessionManager() { return searchSessionManager; }
}
