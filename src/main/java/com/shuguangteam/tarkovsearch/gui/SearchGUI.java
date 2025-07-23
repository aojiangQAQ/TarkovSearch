package com.shuguangteam.tarkovsearch.gui;

import com.shuguangteam.tarkovsearch.TarkovSearch;
import com.shuguangteam.tarkovsearch.data.GeneratedItem;
import com.shuguangteam.tarkovsearch.data.Rarity;
import com.shuguangteam.tarkovsearch.util.ItemUtils;
import com.shuguangteam.tarkovsearch.util.ItemUtils;
import com.shuguangteam.tarkovsearch.util.Lang;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class SearchGUI implements InventoryHolder {

    private final Inventory inv;
    private final Lang      lang = TarkovSearch.getInstance().getLang();

    public SearchGUI(Player p, java.util.List<GeneratedItem> items) {
        inv = Bukkit.createInventory(this, 54, lang.get("search-title"));
        ItemStack gray = ItemUtils.glass(Material.GRAY_STAINED_GLASS_PANE, " ");
        for (int i = 0; i < 54; i++) inv.setItem(i, gray);
        p.openInventory(inv);
    }

    @Override public Inventory getInventory() { return inv; }

    /* ---------- 动画接口 ---------- */
    public void showScanning(int slot) {
        ItemStack spy = new ItemStack(Material.SPYGLASS);
        ItemUtils.setName(spy, "§e扫描中...");
        inv.setItem(slot, spy);
    }

    public void showFound(GeneratedItem gi) {
        Material glass = ItemUtils.rarityPane(gi.getRarity());
        String   name  = ItemUtils.coloredRarityName(gi.getRarity());
        ItemStack pane = ItemUtils.glass(glass, name);
        inv.setItem(gi.getSlot(), pane);
    }
}
