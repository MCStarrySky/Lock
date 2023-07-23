package org.sct.lock.util.function;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.sct.lock.data.LibData;

import java.util.concurrent.TimeUnit;

/**
 * @author LovesAsuna
 * @date 2020/2/20 19:08
 */

public class Inhibition {
    public static boolean getInhibitStatus(OfflinePlayer player, int delay, TimeUnit timeUnit) {
        if (LibData.getInhibition().get(player) != null) {
            return false;
        } else {
            LibData.getInhibition().put(player,true);
            LibData.getScheduledpool().schedule(() -> {
                LibData.getInhibition().remove(player);
            }, delay, timeUnit);
            return true;
        }
    }

    @SuppressWarnings("deprecation")
    public static boolean getInhibitStatus(int delay, TimeUnit timeUnit) {
        return getInhibitStatus(Bukkit.getOfflinePlayer("Native"), delay, timeUnit);
    }
}