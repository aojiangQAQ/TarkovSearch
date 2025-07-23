package com.shuguangteam.tarkovsearch.listener;

import com.shuguangteam.tarkovsearch.TarkovSearch;
import com.shuguangteam.tarkovsearch.gui.SearchGUI;
import com.shuguangteam.tarkovsearch.gui.SettingGUI;
import com.shuguangteam.tarkovsearch.manager.SearchSession;
import com.shuguangteam.tarkovsearch.util.ItemUtils;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * 统一监听 SettingGUI 与 SearchGUI 的点击
 */
public class InventoryListener implements Listener {

    private final TarkovSearch plugin;
    public InventoryListener(TarkovSearch plugin) { this.plugin = plugin; }

    /* ---------------- SettingGUI ---------------- */
    @EventHandler
    public void onSettingClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player p)) return;
        if (!(e.getInventory().getHolder() instanceof SettingGUI gui)) return;

        e.setCancelled(true);
        gui.handleClick(p, e.getSlot(), e.getClick(), e.getCurrentItem(), e.getCursor());
    }

    /* ---------------- SearchGUI ---------------- */
    @EventHandler
    public void onSearchClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player p)) return;
        if (!(e.getInventory().getHolder() instanceof SearchGUI)) return;

        e.setCancelled(true);

        SearchSession ss = plugin.getSearchSessionManager().get(p);
        if (ss == null) return;

        int slot = e.getSlot();
        if (!ss.canTake(slot)) {
            p.sendMessage(plugin.getLang().msg("cant-take-yet"));
            return;
        }

        /* 领取真实物品 */
        var box = plugin.getBoxManager().getBox(ss.getBoxLoc());
        var gi  = box.getGeneratedItems().stream()
                .filter(g -> g.getSlot() == slot).findFirst().orElse(null);
        if (gi == null) return;

        ItemStack reward = gi.getItem().clone();
        ss.markTaken(slot);
        p.getInventory().addItem(reward);
        e.getInventory().setItem(slot, null);

        p.sendMessage(plugin.getLang().msg("loot-get")
                .replace("%item%", ItemUtils.getDisplayName(reward))
                .replace("%amount%", String.valueOf(reward.getAmount()))
        );
        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.8F, 1.3F);

        /* 若全部取完 → 冷却 */
        if (ss.allTaken()) {
            long cdSec = plugin.getConfig().getLong("cooldown", 600);
            box.setCooldownEnd(System.currentTimeMillis() + cdSec * 1000L);
            plugin.getBoxManager().saveBoxes();
            p.sendMessage(plugin.getLang().msg("all-taken"));
            ss.stop();
        }
    }
}
