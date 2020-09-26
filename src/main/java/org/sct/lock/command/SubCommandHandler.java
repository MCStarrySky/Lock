package org.sct.lock.command;

import org.bukkit.plugin.java.JavaPlugin;
import org.sct.easylib.util.function.command.CommandHandler;
import org.sct.lock.command.sub.*;

/**
 * @author LovesAsuna
 * @date 2020/3/24 11:50 AM
 */

public class SubCommandHandler extends CommandHandler {
    public SubCommandHandler(JavaPlugin instance, String cmd) {
        super(instance, cmd);
        registerSubCommand("addtype", new AddType());
        registerSubCommand("help", new Help());
        registerSubCommand("info", new Info());
        registerSubCommand("reload", new Reload());
        registerSubCommand("update", new Update());
    }

}


