package com.shuguangteam.tarkovsearch.util;

import com.shuguangteam.tarkovsearch.data.Rarity;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * ItemStack 常用工具
 */
public final class ItemUtils {
    private ItemUtils() {}

    /* ---------- 基础操作 ---------- */

    public static void setName(ItemStack it, String name) {
        ItemMeta m = it.getItemMeta();
        m.setDisplayName(Text.color(name));
        it.setItemMeta(m);
    }
    public static void setLore(ItemStack it, List<String> lore) {
        ItemMeta m = it.getItemMeta();
        m.setLore(lore);
        it.setItemMeta(m);
    }

    /* ---------- 稀有度处理 ---------- */

    public static void addRarityLore(ItemStack it, Rarity r) {
        ItemMeta m = it.getItemMeta();
        List<String> lore = m.hasLore() ? new ArrayList<>(m.getLore()) : new ArrayList<>();
        lore.removeIf(l -> Text.color(l).contains("稀有度:"));
        lore.add(Text.color("&7稀有度: &f" + r.name()));
        m.setLore(lore);
        m.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        it.setItemMeta(m);
    }

    public static Rarity extractRarity(ItemStack it) {
        ItemMeta m = it.getItemMeta();
        if (m == null || !m.hasLore()) return Rarity.普通;
        for (String l : m.getLore()) {
            String s = Text.color(l).replace("§7稀有度: §f", "");
            try { return Rarity.valueOf(s); } catch (IllegalArgumentException ignored) {}
        }
        return Rarity.普通;
    }

    public static ItemStack stripRarityLore(ItemStack src) {
        ItemStack cp = src.clone();
        ItemMeta m = cp.getItemMeta();
        if (m != null && m.hasLore()) {
            List<String> lore = new ArrayList<>(m.getLore());
            lore.removeIf(l -> Text.color(l).contains("稀有度:"));
            m.setLore(lore.isEmpty() ? null : lore);
            cp.setItemMeta(m);
        }
        return cp;
    }

    /* ---------- 玻璃板生成 ---------- */

    public static ItemStack glass(Material mat, String name) {
        ItemStack it = new ItemStack(mat);
        setName(it, name);
        return it;
    }

    public static Material rarityPane(Rarity r) {
        return switch (r) {
            case 稀有 -> Material.GREEN_STAINED_GLASS_PANE;
            case 罕见 -> Material.LIGHT_BLUE_STAINED_GLASS_PANE;
            case 史诗 -> Material.PURPLE_STAINED_GLASS_PANE;
            case 珍品 -> Material.ORANGE_STAINED_GLASS_PANE;
            case 仙品 -> Material.RED_STAINED_GLASS_PANE;
            default   -> Material.WHITE_STAINED_GLASS_PANE;
        };
    }

    public static String coloredRarityName(Rarity r) {
        return switch (r) {
            case 稀有 -> "§a稀有";
            case 罕见 -> "§9罕见";
            case 史诗 -> "§5史诗";
            case 珍品 -> "§6珍品";
            case 仙品 -> "§c仙品";
            default   -> "§f普通";
        };
    }

    /* ---------- 友好名称 ---------- */

    /** 获取聊天提示用的显示名称，优先自定义名，否则翻译名 */
    public static String getDisplayName(ItemStack it) {
        ItemMeta m = it.getItemMeta();
        if (m != null && m.hasDisplayName()) return m.getDisplayName();
        return it.getI18NDisplayName();   // Paper/Spigot 自动翻译
    }
}
