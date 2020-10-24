package org.sct.lock.util.function;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.sct.lock.enumeration.ConfigType;
import org.sct.lock.file.Config;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LovesAsuna
 */
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

    public static List<Entity> getNearbyEntities(Location loc, int range) {
        World world = loc.getWorld();
        return new ArrayList<>(world.getNearbyEntities(loc, range, range, range));
    }
}
