package org.sct.lock;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.sct.lock.command.SubCommandHandler;
import org.sct.lock.data.LockData;
import org.sct.lock.enumeration.ConfigType;
import org.sct.lock.file.Lang;
import org.sct.lock.util.ListenerManager;
import org.sct.plugincore.PluginCore;
import org.sct.plugincore.PluginCoreAPI;
import org.sct.plugincore.util.function.econoomy.EcoUtil;
import org.sct.plugincore.util.plugin.CheckUpdate;
import org.sct.plugincore.util.plugin.FileUpdate;
import org.sct.plugincore.util.plugin.Metrics;


/**
 * @author alchemy
 * @since 2019/12/2 17:15:17
 */
public final class Lock extends JavaPlugin {

    @Getter
    private static LockData lockData;
    @Getter
    private static Lock instance;
    @Getter
    private static PluginCoreAPI pluginCoreAPI;

    @Override
    public void onEnable() {
        instance = this;
        Metrics metrics = new Metrics(this, 6910);
        lockData = new LockData();
        pluginCoreAPI = PluginCore.getPluginCoreAPI();
        ListenerManager.register();
        Lang.loadLang();
        EcoUtil.loadVault();
        LockData.getPool().submit(() -> {
            FileUpdate.update(instance, "config.yml", getDataFolder().getPath());
            FileUpdate.update(instance, getConfig().getString(ConfigType.SETTING_LANGUAGE.getPath()) + ".yml", getDataFolder().getPath());
            CheckUpdate.check(Bukkit.getConsoleSender(), instance, "LovesAsuna", "ZDRlZWY4ZDZlMzIyNDExYjk3NThlMGNiN2ZmYzg3NTRiOGIwZDUzZA==");
        });
        saveDefaultConfig();
        Bukkit.getPluginCommand("lock").setExecutor(new SubCommandHandler(instance, "Lock"));
        getServer().getConsoleSender().sendMessage("      ___       ___           ___           ___     ");
        getServer().getConsoleSender().sendMessage("     /\\__\\     /\\  \\         /\\  \\         /\\__\\");
        getServer().getConsoleSender().sendMessage("    /:/  /    /::\\  \\       /::\\  \\       /:/  /");
        getServer().getConsoleSender().sendMessage("   /:/  /    /:/\\:\\  \\     /:/\\:\\  \\     /:/__/");
        getServer().getConsoleSender().sendMessage("  /:/  /    /:/  \\:\\  \\   /:/  \\:\\  \\   /::\\__\\____");
        getServer().getConsoleSender().sendMessage(" /:/__/    /:/__/ \\:\\__\\ /:/__/ \\:\\__\\ /:/\\:::::\\__\\");
        getServer().getConsoleSender().sendMessage(" \\:\\  \\    \\:\\  \\ /:/  / \\:\\  \\  \\/__/ \\/_|:|~~|~");
        getServer().getConsoleSender().sendMessage("  \\:\\  \\    \\:\\  /:/  /   \\:\\  \\          |:|  |");
        getServer().getConsoleSender().sendMessage("   \\:\\__\\    \\::/  /       \\:\\__\\         |:|  |");
        getServer().getConsoleSender().sendMessage("    \\/__/     \\/__/         \\/__/          \\|__|");
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage("§7[§eLock§7]§c插件已被卸载");
    }

}
