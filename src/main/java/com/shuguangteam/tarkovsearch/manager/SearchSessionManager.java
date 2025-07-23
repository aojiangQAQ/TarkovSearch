package com.shuguangteam.tarkovsearch.manager;

import com.shuguangteam.tarkovsearch.TarkovSearch;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SearchSessionManager {

    private final Map<UUID, SearchSession> sessions = new HashMap<>();

    public SearchSessionManager(TarkovSearch tarkovSearch) {
    }

    public SearchSession get(Player p) {
        return sessions.get(p.getUniqueId());
    }

    public void set(Player p, SearchSession s) {
        sessions.put(p.getUniqueId(), s);
    }

    public void remove(Player p) {
        SearchSession ss = sessions.remove(p.getUniqueId());
        if (ss != null) ss.stop();
    }
}
