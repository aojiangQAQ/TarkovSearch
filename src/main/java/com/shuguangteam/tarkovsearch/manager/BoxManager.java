package com.shuguangteam.tarkovsearch.manager;

import com.shuguangteam.tarkovsearch.TarkovSearch;
import com.shuguangteam.tarkovsearch.data.*;
import com.shuguangteam.tarkovsearch.util.LocationUtils;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 物资箱管理：加载/保存与随机生成物品
 */
public class BoxManager {

    /* ----------------- 字段 ----------------- */
    private final TarkovSearch plugin;
    private final Map<String, BoxData> boxes = new HashMap<>();
    private final File file;

    /* ----------------- 构造 ----------------- */
    public BoxManager(TarkovSearch plugin) {
        this.plugin = plugin;
        this.file   = new File(plugin.getDataFolder(), "boxes.yml");
        loadBoxes();
    }

    /* ===================================================== */
    /*                     持久化 (YAML)                      */
    /* ===================================================== */

    public void loadBoxes() {
        boxes.clear();
        if (!file.exists()) return;
        YamlConfiguration yc = YamlConfiguration.loadConfiguration(file);
        yc.getKeys(false).forEach(k -> boxes.put(k, (BoxData) yc.get(k)));
    }

    public void saveBoxes() {
        YamlConfiguration yc = new YamlConfiguration();
        boxes.forEach(yc::set);
        try { yc.save(file); } catch (Exception e) { e.printStackTrace(); }
    }

    /* ===================================================== */
    /*                        CRUD                           */
    /* ===================================================== */

    public boolean isBox(Location loc)          { return boxes.containsKey(LocationUtils.key(loc)); }
    public BoxData getBox(Location loc)         { return boxes.get(LocationUtils.key(loc)); }

    public BoxData createBox(Location loc) {
        BoxData bd = new BoxData(UUID.randomUUID(), loc);
        boxes.put(LocationUtils.key(loc), bd);
        saveBoxes();
        return bd;
    }

    public void deleteBox(Location loc) {
        boxes.remove(LocationUtils.key(loc));
        saveBoxes();
    }

    /* ===================================================== */
    /*                随机生成物品（已修复）                   */
    /* ===================================================== */

    /**
     * 生成流程：<br>
     * ① 读取配置概率并归一化；<br>
     * ② 遍历物品池，按物品自身稀有度概率决定是否出现；<br>
     * ③ 若无物品抽中，则随机保底 1 件；<br>
     * ④ 随机槽位、保持物品原稀有度 → 写回 BoxData。
     */
    public void generateItems(BoxData box) {

        List<StoredItem> pool = box.getPoolItems();
        if (pool.isEmpty()) return;

        /* ---------- 1. 构造原始概率表 ---------- */
        Map<Rarity, Double> prob = new EnumMap<>(Rarity.class);
        double sum = 0;
        for (Rarity r : Rarity.values()) {
            double p = plugin.getConfig().getDouble("probability." + r.name(), 1.0);
            prob.put(r, p);
            sum += p;
        }

        /* ---------- 2. 归一化（使用 total 常量，避免 lambda 报错） ---------- */
        final double total = sum;                           // ← 关键修复
        prob.replaceAll((r, v) -> v / total);               // 0~1 概率

        /* ---------- 3. 抽取出现的物品 ---------- */
        List<Integer> slots = new ArrayList<>();
        for (int i = 0; i < 54; i++) slots.add(i);
        Collections.shuffle(slots);

        List<GeneratedItem> gens = new ArrayList<>();
        ThreadLocalRandom rnd = ThreadLocalRandom.current();

        for (StoredItem si : pool) {
            double chance = prob.get(si.getRarity());
            if (rnd.nextDouble() <= chance) {
                int slot = slots.remove(0);
                gens.add(new GeneratedItem(si.getItem(), si.getRarity(), slot));
            }
        }

        /* ---------- 4. 保底至少 1 件 ---------- */
        if (gens.isEmpty()) {
            StoredItem si = pool.get(rnd.nextInt(pool.size()));
            int slot = slots.remove(0);
            gens.add(new GeneratedItem(si.getItem(), si.getRarity(), slot));
        }

        /* ---------- 5. 写回 BoxData ---------- */
        box.setGeneratedItems(gens);
    }
}
