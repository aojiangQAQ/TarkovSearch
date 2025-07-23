package com.shuguangteam.tarkovsearch.data;

public enum Rarity {
    普通,
    稀有,
    罕见,
    史诗,
    珍品,
    仙品;

    // 下一个稀有度，用于右键循环
    public Rarity next() {
        int idx = this.ordinal() + 1;
        if (idx >= values().length) idx = 0;
        return values()[idx];
    }
}
