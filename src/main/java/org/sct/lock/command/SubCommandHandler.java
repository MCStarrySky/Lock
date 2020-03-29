package org.sct.lock.command;


import org.bukkit.plugin.java.JavaPlugin;
import org.sct.lock.command.sub.*;
import org.sct.plugincore.util.function.command.CommandHandler;

/**
 * @author LovesAsuna
 * @date 2020/3/24 11:50 AM
 */

public class SubCommandHandler extends CommandHandler {
    public SubCommandHandler(JavaPlugin instance, String cmd) {
        super(instance, cmd);
        registerSubCommand("addtype", new addType());
        registerSubCommand("help", new Help());
        registerSubCommand("info", new Info());
        registerSubCommand("reload", new Reload());
        registerSubCommand("update", new Update());
    }

}


