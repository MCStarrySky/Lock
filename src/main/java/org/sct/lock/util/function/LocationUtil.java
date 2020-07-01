package org.sct.lock.util.function;

import org.bukkit.Location;
import org.sct.lock.enumeration.ConfigType;
import org.sct.lock.file.Config;

public class LocationUtil {
    public static boolean checkWorld(Location location) {
        boolean in = false;
        for (String world : Config.getStringList(ConfigType.SETTING_WORLDS.getPath())) {
            if (world.equals(location.getWorld().getName())) {
                in = true;
            }
        }
        return in;
    }
}
