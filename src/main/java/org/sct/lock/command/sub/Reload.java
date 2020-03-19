package org.sct.lock.command.sub;

import org.bukkit.command.CommandSender;

import org.sct.lock.Lock;
import org.sct.lock.enumeration.LangType;
import org.sct.lock.file.Config;
import org.sct.lock.file.Lang;
import org.sct.plugincore.util.function.SubCommand;

public class Reload implements SubCommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(Lang.getString(LangType.LANG_NOPERMISSION));
        }

        Lang.loadLang();
        Lock.getInstance().saveDefaultConfig();
        Config.loadConfig();

        sender.sendMessage(Lang.getString(LangType.LANG_RELOAD));
        return true;
    }

}
