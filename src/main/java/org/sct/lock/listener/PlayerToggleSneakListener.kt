package org.sct.lock.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import org.sct.lock.data.LockData;

public class PlayerToggleSneakListener implements Listener {

    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent e) {
        //如果玩家正在前行
        if (e.isSneaking()) {
            LockData.getPlayerisSneak().put(e.getPlayer(),true);
        } else {
            LockData.getPlayerisSneak().remove(e.getPlayer());
        }
    }
}
