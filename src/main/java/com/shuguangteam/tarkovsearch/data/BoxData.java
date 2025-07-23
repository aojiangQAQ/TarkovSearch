package com.shuguangteam.tarkovsearch.data;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.*;

/**
 * 一个物资箱的数据对象
 */
public class BoxData implements ConfigurationSerializable {
    private final UUID id;
    private Location location;
    private List<StoredItem> poolItems; // 设置GUI中定义的物品池

    // 当前生成的物品（在冷却前固定）
    private List<GeneratedItem> generatedItems = new ArrayList<>();

    // 冷却结束时间戳（毫秒）
    private long cooldownEnd = 0L;

    public BoxData(UUID id, Location location) {
        this.id = id;
        this.location = location;
        this.poolItems = new ArrayList<>();
    }

    public UUID getId() { return id; }
    public Location getLocation() { return location; }

    public List<StoredItem> getPoolItems() { return poolItems; }
    public void setPoolItems(List<StoredItem> poolItems) { this.poolItems = poolItems; }

    public List<GeneratedItem> getGeneratedItems() { return generatedItems; }
    public void setGeneratedItems(List<GeneratedItem> generatedItems) { this.generatedItems = generatedItems; }

    public long getCooldownEnd() { return cooldownEnd; }
    public void setCooldownEnd(long cooldownEnd) { this.cooldownEnd = cooldownEnd; }

    public boolean isInCooldown() {
        return System.currentTimeMillis() < cooldownEnd;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id.toString());
        map.put("location", location);
        map.put("poolItems", poolItems);
        map.put("cooldownEnd", cooldownEnd);
        // generatedItems 不序列化，重启后重新生成（也可序列化，看你需求，这里简单处理）。
        return map;
    }

    public static BoxData deserialize(Map<String, Object> map) {
        UUID id = UUID.fromString((String) map.get("id"));
        Location loc = (Location) map.get("location");
        List<StoredItem> pool = (List<StoredItem>) map.get("poolItems");
        long cd = (long) map.getOrDefault("cooldownEnd", 0L);
        BoxData data = new BoxData(id, loc);
        data.setPoolItems(pool != null ? pool : new ArrayList<>());
        data.setCooldownEnd(cd);
        return data;
    }
}
