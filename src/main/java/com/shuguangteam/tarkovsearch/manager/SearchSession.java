package com.shuguangteam.tarkovsearch.manager;

import com.shuguangteam.tarkovsearch.TarkovSearch;
import com.shuguangteam.tarkovsearch.data.GeneratedItem;
import com.shuguangteam.tarkovsearch.data.SearchSlotState;
import com.shuguangteam.tarkovsearch.gui.SearchGUI;
import com.shuguangteam.tarkovsearch.util.SchedulerUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 描述：一次搜索动画的完整会话。
 *
 * 1. SearchGUI 打开后创建此对象并调用 {@link #start()}。
 * 2. 内部使用 BukkitScheduler 逐格演示“灰板 → 望远镜 → 彩板”动画。
 * 3. InventoryListener 通过 {@link #canTake(int)} 判断玩家是否可取物，
 *    取出后调用 {@link #markTaken(int)} 标记。
 * 4. {@link #allTaken()} 为 true 时由监听器触发冷却并 stop()。
 */
public class SearchSession {

    /* -------------------- 固定数据 -------------------- */
    private final Player            player;
    private final SearchGUI         gui;
    private final List<GeneratedItem> items;         // 生成物品列表（位置固定）
    private final Location          boxLoc;          // 物资箱方块坐标

    /* -------------------- 状态数据 -------------------- */
    private final Map<Integer, SearchSlotState> slotStates = new HashMap<>();
    private int       index = 0;                     // 当前动画到第几个物品
    private BukkitTask task;                         // 定时任务句柄

    /* -------------------- 构造函数 -------------------- */
    public SearchSession(Player            player,
                         SearchGUI         gui,
                         List<GeneratedItem> items,
                         Location          boxLoc) {

        this.player = player;
        this.gui    = gui;
        this.items  = items;
        this.boxLoc = boxLoc;

        // 初始全部 HIDDEN
        items.forEach(g -> slotStates.put(g.getSlot(), SearchSlotState.HIDDEN));
    }

    /* -------------------- 对外接口 -------------------- */

    /** 开始动画 */
    public void start() { nextStep(); }

    /** 玩家背包点击时判断是否可领取 */
    public boolean canTake(int slot) {
        return slotStates.getOrDefault(slot, SearchSlotState.HIDDEN) == SearchSlotState.FOUND;
    }

    /** 领取后标记 TAKEN */
    public void markTaken(int slot) { slotStates.put(slot, SearchSlotState.TAKEN); }

    /** 是否全部领取完毕 */
    public boolean allTaken() { return slotStates.values().stream().allMatch(s -> s == SearchSlotState.TAKEN); }

    /** 物资箱坐标（InventoryListener 需要） */
    public Location getBoxLoc() { return boxLoc; }

    /** 停止动画（取完或退出界面时调用） */
    public void stop() { if (task != null) task.cancel(); }

    /* -------------------- 动画内部 -------------------- */
    private void nextStep() {
        if (index >= items.size()) return;

        GeneratedItem gi   = items.get(index);
        int           slot = gi.getSlot();

        /* 1. 灰→望远镜 */
        slotStates.put(slot, SearchSlotState.SCANNING);
        gui.showScanning(slot);

        /* 2. 延迟 1~5 秒后 望远镜→彩板 */
        TarkovSearch plg = TarkovSearch.getInstance();
        long minTick = plg.getConfig().getInt("search.min_seconds", 1) * 20L;
        long maxTick = plg.getConfig().getInt("search.max_seconds", 5) * 20L;
        long delay   = ThreadLocalRandom.current().nextLong(minTick, maxTick + 1);

        task = SchedulerUtils.runLater(() -> {
            slotStates.put(slot, SearchSlotState.FOUND);
            gui.showFound(gi);

            index++;
            nextStep();        // 递归到下一格
        }, delay);
    }
}
