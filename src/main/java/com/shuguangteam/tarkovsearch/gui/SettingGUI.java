package com.shuguangteam.tarkovsearch.gui;

import com.shuguangteam.tarkovsearch.TarkovSearch;
import com.shuguangteam.tarkovsearch.data.BoxData;
import com.shuguangteam.tarkovsearch.data.Rarity;
import com.shuguangteam.tarkovsearch.data.StoredItem;
import com.shuguangteam.tarkovsearch.util.ItemUtils;
import com.shuguangteam.tarkovsearch.util.Lang;
import com.shuguangteam.tarkovsearch.util.ItemUtils;
import com.shuguangteam.tarkovsearch.util.Text;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/** 物资箱设置 GUI */
public class SettingGUI implements InventoryHolder {

    private static final TarkovSearch plugin = TarkovSearch.getInstance();
    private static final Lang         lang   = plugin.getLang();

    private final BoxData   boxData;
    private final Inventory inv;
    private final int       CONFIRM_SLOT;

    private SettingGUI(BoxData boxData) {
        this.boxData = boxData;

        int size = plugin.getConfig().getInt("setting-gui-size", 54);
        if (size != 27 && size != 54) size = 54;
        inv = Bukkit.createInventory(this, size, lang.get("setting-title"));
        CONFIRM_SLOT = size - 1;

        loadItems();
        placeConfirm();
    }

    public static void open(Player p, BoxData boxData) { p.openInventory(new SettingGUI(boxData).inv); }

    @Override public Inventory getInventory() { return inv; }

    /* -------------------------- 初始渲染 -------------------------- */
    private void loadItems() {
        int idx = 0;
        for (StoredItem si : boxData.getPoolItems()) {
            if (idx >= inv.getSize() - 1) break;
            ItemStack show = si.getItem().clone();
            ItemUtils.addRarityLore(show, si.getRarity());
            inv.setItem(idx++, show);
        }
    }
    private void placeConfirm() {
        ItemStack ok = new ItemStack(Material.LIME_CONCRETE);
        ItemUtils.setName(ok, lang.get("confirm-button-name"));
        ItemUtils.setLore(ok, lang.getList("confirm-button-lore"));
        inv.setItem(CONFIRM_SLOT, ok);
    }

    /* -------------------------- 点击处理 -------------------------- */
    public void handleClick(Player p, int slot, ClickType type,
                            ItemStack current, ItemStack cursor) {

        if (slot == CONFIRM_SLOT && type == ClickType.DOUBLE_CLICK) {
            saveBoxData();
            p.closeInventory();
            return;
        }
        if (slot == CONFIRM_SLOT) return;

        if (type.isLeftClick()) {
            if (cursor != null && cursor.getType() != Material.AIR) {
                inv.setItem(slot, cursor.clone());
                p.setItemOnCursor(null);
                ItemUtils.addRarityLore(inv.getItem(slot), Rarity.普通);
            } else if (current != null && current.getType() != Material.AIR) {
                p.setItemOnCursor(current.clone());
                inv.setItem(slot, null);
            }
            return;
        }
        if (type.isRightClick() && current != null && current.getType() != Material.AIR) {
            Rarity next = ItemUtils.extractRarity(current).next();
            ItemUtils.addRarityLore(current, next);
        }
    }

    /* -------------------------- 保存逻辑 -------------------------- */
    private void saveBoxData() {
        List<StoredItem> list = new ArrayList<>();
        for (int i = 0; i < inv.getSize(); i++) {
            if (i == CONFIRM_SLOT) continue;
            ItemStack guiItem = inv.getItem(i);
            if (guiItem == null || guiItem.getType() == Material.AIR) continue;

            Rarity rarity = ItemUtils.extractRarity(guiItem);
            ItemStack clean = ItemUtils.stripRarityLore(guiItem);
            list.add(new StoredItem(clean, rarity));
        }
        boxData.setPoolItems(list);
        plugin.getBoxManager().saveBoxes();
    }
}
