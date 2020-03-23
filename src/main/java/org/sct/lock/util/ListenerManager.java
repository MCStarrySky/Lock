package org.sct.lock.util;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import org.sct.lock.Lock;
import org.sct.lock.listener.*;

public class ListenerManager {

    private static void register(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, Lock.getInstance());
    }

    /**
     * 注册监听器
     */
    public static void register() {
        register(new PlayerInteractListener());
        register(new SignChangeListener());
        register(new PlayerToggleSneakListener());
        register(new LockDoorAccessListener());
        register(new BlockRedstone());
        register(new BlockBreak());
    }

}
