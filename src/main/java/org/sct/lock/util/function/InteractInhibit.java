package org.sct.lock.util.function;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.sct.lock.data.LockData;

import java.util.concurrent.TimeUnit;

/**
 * @author LovesAsuna
 * @date 2020/4/3 12:53
 */

public class InteractInhibit {

    public static boolean getInhibitStatus(Player player, int milliseconds) {
        return getInhibitStatus(player.getName(), milliseconds);
    }

    @SuppressWarnings("deprecation")
    public static boolean getInhibitStatus(String playerName, int milliseconds) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(playerName);
        if (LockData.INSTANCE.getInhibition().get(player) != null) {
            return false;
        } else {
            LockData.INSTANCE.getInhibition().put(player,true);
            LockData.INSTANCE.getScheduledpool().schedule(() -> {
                LockData.INSTANCE.getInhibition().remove(player);
            }, milliseconds, TimeUnit.MILLISECONDS);
            return true;
        }
    }
}
