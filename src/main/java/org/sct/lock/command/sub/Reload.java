package org.sct.lock.command.sub;

import org.bukkit.command.CommandSender;
import org.sct.easylib.util.function.command.SubCommand;
import org.sct.lock.Lock;
import org.sct.lock.enumeration.LangType;
import org.sct.lock.file.Config;
import org.sct.lock.file.Lang;

import java.util.Map;

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

    @Override
    public Map<Integer, String[]> getParams() {
        return null;
    }

}
