package org.sct.lock

//import org.sct.easylib.EasyLib
//import org.sct.easylib.EasyLibAPI
//import org.sct.easylib.util.plugin.CheckUpdate
//import org.sct.easylib.util.plugin.FileUpdate
///import org.sct.easylib.util.plugin.Metrics
//import org.sct.easylib.util.reflectutil.ClassUtil
import taboolib.common.platform.Plugin
import taboolib.common.platform.function.console
import taboolib.platform.util.bukkitPlugin


/**
 * @author alchemy
 * @since 2019/12/2 17:15:17
 */
object Lock : Plugin(){
    override fun onEnable() {
       // Metrics(this, 6910)
        //easyLibAPI = EasyLib.getEasyLibAPI()
      //  ListenerManager.register()
     //   ClassUtil.forceLoad(Lang::class.java)
      //  Lang.load()
     //   easyLibAPI.ecoAPI.loadVault()
      //  pool.submit {
     //       FileUpdate.update(instance, "config.yml", dataFolder.path)
     //       FileUpdate.update(instance, config.getString(ConfigType.SETTING_LANGUAGE.path) + ".yml", dataFolder.path)
     //       CheckUpdate.check(Bukkit.getConsoleSender(), instance, "LovesAsuna", "ZDRlZWY4ZDZlMzIyNDExYjk3NThlMGNiN2ZmYzg3NTRiOGIwZDUzZA==")
     //   }
        bukkitPlugin.saveDefaultConfig()
        //getCommand("lock").executor = SubCommandHandler(instance, "Lock")
        console().sendMessage("      ___       ___           ___           ___     ")
        console().sendMessage("     /\\__\\     /\\  \\         /\\  \\         /\\__\\")
        console().sendMessage("    /:/  /    /::\\  \\       /::\\  \\       /:/  /")
        console().sendMessage("   /:/  /    /:/\\:\\  \\     /:/\\:\\  \\     /:/__/")
        console().sendMessage("  /:/  /    /:/  \\:\\  \\   /:/  \\:\\  \\   /::\\__\\____")
        console().sendMessage(" /:/__/    /:/__/ \\:\\__\\ /:/__/ \\:\\__\\ /:/\\:::::\\__\\")
        console().sendMessage(" \\:\\  \\    \\:\\  \\ /:/  / \\:\\  \\  \\/__/ \\/_|:|~~|~")
        console().sendMessage("  \\:\\  \\    \\:\\  /:/  /   \\:\\  \\          |:|  |")
        console().sendMessage("   \\:\\__\\    \\::/  /       \\:\\__\\         |:|  |")
        console().sendMessage("    \\/__/     \\/__/         \\/__/          \\|__|")
    }

    override fun onDisable() {
        console().sendMessage("§7[§eLock§7]§c插件已被卸载")
    }
}