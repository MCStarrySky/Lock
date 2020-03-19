package org.sct.lock.command.sub;

import org.bukkit.command.CommandSender;

import org.sct.lock.enumeration.LangType;
import org.sct.lock.file.Lang;
import org.sct.plugincore.util.function.SubCommand;

import java.util.List;

public class Help implements SubCommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        List<String> helpList = Lang.getStringList(LangType.LANGUAGE_COMMANDHELP);

        for (String cmd : helpList) {
            sender.sendMessage(cmd);
        }
        return true;
    }

}
