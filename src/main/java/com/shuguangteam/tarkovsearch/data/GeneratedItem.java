package com.shuguangteam.tarkovsearch.data;

import org.bukkit.inventory.ItemStack;

/**
 * 某次生成时的物品（固定），包括其随机到的槽位
 */
public class GeneratedItem {
    private final ItemStack item;
    private final Rarity rarity;
    private final int slot;

    public GeneratedItem(ItemStack item, Rarity rarity, int slot) {
        this.item = item;
        this.rarity = rarity;
        this.slot = slot;
    }

    public ItemStack getItem() { return item.clone(); }
    public Rarity getRarity() { return rarity; }
    public int getSlot() { return slot; }
}
