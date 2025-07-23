package com.shuguangteam.tarkovsearch.listener;

import com.shuguangteam.tarkovsearch.TarkovSearch;
import com.shuguangteam.tarkovsearch.gui.SearchGUI;
import com.shuguangteam.tarkovsearch.manager.BoxManager;
import com.shuguangteam.tarkovsearch.manager.SearchSession;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {

    private final TarkovSearch plugin;
    private final BoxManager   boxes;

    public PlayerInteractListener(TarkovSearch plugin) {
        this.plugin = plugin;
        this.boxes  = plugin.getBoxManager();
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Block b = e.getClickedBlock();
        if (b == null || !(b.getState() instanceof Chest)) return;

        if (!boxes.isBox(b.getLocation())) return;
        e.setCancelled(true);

        var box = boxes.getBox(b.getLocation());
        Player p = e.getPlayer();

        /* 冷却判定 */
        if (box.isInCooldown()) {
            p.sendMessage(plugin.getLang().msg("search-cooldown"));
            return;
        }

        /* 首次打开需随机生成 */
        if (box.getGeneratedItems().isEmpty()) {
            boxes.generateItems(box);
            if (box.getGeneratedItems().isEmpty()) {
                p.sendMessage(plugin.getLang().msg("search-empty"));
                return;
            }
        }

        /* 打开 GUI 并创建新的 SearchSession —— 4 个参数 */
        SearchGUI gui = new SearchGUI(p, box.getGeneratedItems());
        SearchSession ss = new SearchSession(
                p,
                gui,
                box.getGeneratedItems(),
                b.getLocation());            // ← 新增：物资箱坐标
        plugin.getSearchSessionManager().set(p, ss);
        ss.start();
    }
}
