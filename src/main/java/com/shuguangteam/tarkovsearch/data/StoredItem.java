package com.shuguangteam.tarkovsearch.data;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * 存储在设置GUI里的条目：物品 + 稀有度
 */
public class StoredItem implements ConfigurationSerializable {
    private ItemStack item;
    private Rarity rarity;

    public StoredItem(ItemStack item, Rarity rarity) {
        this.item = item;
        this.rarity = rarity;
    }

    public ItemStack getItem() {
        return item.clone();
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public void setRarity(Rarity rarity) {
        this.rarity = rarity;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("item", item);
        map.put("rarity", rarity.name());
        return map;
    }

    @SuppressWarnings("unchecked")
    public static StoredItem deserialize(Map<String, Object> map) {
        ItemStack item = (ItemStack) map.get("item");
        Rarity r = Rarity.valueOf((String) map.get("rarity"));
        return new StoredItem(item, r);
    }
}
