package org.sct.lock

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.sct.lock.command.SubCommandHandler
import org.sct.lock.data.LockData.pool
import org.sct.lock.enumeration.ConfigType
import org.sct.lock.file.Lang
import org.sct.lock.util.ListenerManager
import org.sct.plugincore.PluginCore
import org.sct.plugincore.PluginCoreAPI
import org.sct.plugincore.util.plugin.CheckUpdate
import org.sct.plugincore.util.plugin.FileUpdate
import org.sct.plugincore.util.plugin.Metrics

/**
 * @author alchemy
 * @since 2019/12/2 17:15:17
 */
class Lock : JavaPlugin() {
    override fun onEnable() {
        instance = this
        Metrics(this, 6910)
        pluginCoreAPI = PluginCore.getPluginCoreAPI()
        ListenerManager.register()
        Lang.loadLang()
        pluginCoreAPI.getEcoAPI().loadVault()
        pool!!.submit {
            FileUpdate.update(instance, "config.yml", dataFolder.path)
            FileUpdate.update(instance, config.getString(ConfigType.SETTING_LANGUAGE.path) + ".yml", dataFolder.path)
            CheckUpdate.check(Bukkit.getConsoleSender(), instance, "LovesAsuna", "ZDRlZWY4ZDZlMzIyNDExYjk3NThlMGNiN2ZmYzg3NTRiOGIwZDUzZA==")
        }
        saveDefaultConfig()
        Bukkit.getPluginCommand("lock").executor = SubCommandHandler(instance, "Lock")
        server.consoleSender.sendMessage("      ___       ___           ___           ___     ")
        server.consoleSender.sendMessage("     /\\__\\     /\\  \\         /\\  \\         /\\__\\")
        server.consoleSender.sendMessage("    /:/  /    /::\\  \\       /::\\  \\       /:/  /")
        server.consoleSender.sendMessage("   /:/  /    /:/\\:\\  \\     /:/\\:\\  \\     /:/__/")
        server.consoleSender.sendMessage("  /:/  /    /:/  \\:\\  \\   /:/  \\:\\  \\   /::\\__\\____")
        server.consoleSender.sendMessage(" /:/__/    /:/__/ \\:\\__\\ /:/__/ \\:\\__\\ /:/\\:::::\\__\\")
        server.consoleSender.sendMessage(" \\:\\  \\    \\:\\  \\ /:/  / \\:\\  \\  \\/__/ \\/_|:|~~|~")
        server.consoleSender.sendMessage("  \\:\\  \\    \\:\\  /:/  /   \\:\\  \\          |:|  |")
        server.consoleSender.sendMessage("   \\:\\__\\    \\::/  /       \\:\\__\\         |:|  |")
        server.consoleSender.sendMessage("    \\/__/     \\/__/         \\/__/          \\|__|")
    }

    override fun onDisable() {
        server.consoleSender.sendMessage("§7[§eLock§7]§c插件已被卸载")
    }

    companion object {
        @JvmStatic
        lateinit var instance: Lock
            private set
        @JvmStatic
        lateinit var pluginCoreAPI: PluginCoreAPI
            private set
    }
}