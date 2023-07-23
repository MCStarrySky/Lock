package org.sct.lock.file;

import org.bukkit.configuration.file.FileConfiguration;
import org.sct.lock.util.BasicUtil;
import org.sct.lock.Lock;
import taboolib.platform.util.BukkitPluginKt;

import java.util.List;

/**
 * @author LovesAsuna
 * @date 2020/7/1 12:29
 */

public class Config {
    private static FileConfiguration getConfig() {
        return BukkitPluginKt.getBukkitPlugin().getConfig();
    }

    public static String getString(String path) {
        reload();
        String string = getConfig().getString(path);
        return string == null ? "Empty String" : string;
    }

    public static void reload() {
        BukkitPluginKt.getBukkitPlugin().reloadConfig();
    }

    public static List<String> getStringList(String path) {
        reload();
        return BasicUtil.convert(getConfig().getStringList(path));
    }

    public static boolean getBoolean(String path) {
        reload();
        return getConfig().getBoolean(path);
    }

    public static double getDouble(String path) {
        reload();
        return getConfig().getDouble(path);
    }

    public static int getInt(String path) {
        reload();
        return (int) getDouble(path);
    }

    public static float getFloat(String path) {
        reload();
        return (float) getDouble(path);
    }

    public static void setStringList(String path, List<String> list) {
        getConfig().set(path, list);
    }
}
