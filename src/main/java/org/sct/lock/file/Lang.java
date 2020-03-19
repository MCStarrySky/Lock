package org.sct.lock.file;

import org.bukkit.configuration.file.YamlConfiguration;
import org.sct.lock.Lock;
import org.sct.lock.enumeration.ConfigType;
import org.sct.lock.enumeration.LangType;
import org.sct.plugincore.util.BasicUtil;
import java.io.File;
import java.util.List;

public class Lang {

    private static File file;
    private static YamlConfiguration config;

    public static void loadLang() {
        file = new File(Lock.getInstance().getDataFolder() + File.separator + Config.getString(ConfigType.SETTING_LANGUAGE) + ".yml");
        if (!file.exists()) { Lock.getInstance().saveResource(Config.getString(ConfigType.SETTING_LANGUAGE) + ".yml",false); }
        config = YamlConfiguration.loadConfiguration(file);
    }

    public static String getString(LangType langType) {
        loadLang();
        return BasicUtil.convert(config.getString(langType.getPath()));
    }

    public static List<String> getStringList(LangType langType) {
        loadLang();
        return BasicUtil.convert(config.getStringList(langType.getPath()));
    }

}
