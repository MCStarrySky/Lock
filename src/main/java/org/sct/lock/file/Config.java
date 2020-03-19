package org.sct.lock.file;

import org.bukkit.configuration.file.FileConfiguration;
import org.sct.lock.Lock;
import org.sct.lock.enumeration.ConfigType;

import java.util.List;

public class Config {

    private static FileConfiguration config = Lock.getInstance().getConfig();

    public static void loadConfig() {
        Lock.getInstance().reloadConfig();
        config = Lock.getInstance().getConfig();
    }

    public static String getString(ConfigType configType) {
        loadConfig();
        return config.getString(configType.getPath());
    }

    public static int getInteger(ConfigType configType) {
        loadConfig();
        return config.getInt(configType.getPath());
    }

    public static boolean getBoolean(ConfigType configType) {
        loadConfig();
        return config.getBoolean(configType.getPath());
    }

    public static List<String> getStringList(ConfigType configType) {
        loadConfig();
        return config.getStringList(configType.getPath());
    }

    public static boolean setStringList(ConfigType configType, List list) {
        loadConfig();
        config.set(configType.getPath(), list);
        return true;
    }
}
