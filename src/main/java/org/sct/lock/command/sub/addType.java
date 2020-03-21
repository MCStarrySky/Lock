package org.sct.lock.command.sub;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.sct.lock.data.LockData;
import org.sct.lock.enumeration.LangType;
import org.sct.lock.file.Lang;
import org.sct.plugincore.util.BasicUtil;
import org.sct.plugincore.util.function.SubCommand;

/**
 * @author LovesAsuna
 * @date 2020/2/15 20:29
 */

public class addType implements SubCommand {
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        boolean error = true;

        if (!(sender instanceof Player)) {
            sender.sendMessage(BasicUtil.convert(Lang.getString(LangType.LANG_NOTAPLAYER)));
            return false;
        }

        if (!sender.isOp()) {
            sender.sendMessage(Lang.getString(LangType.LANG_NOPERMISSION));
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("addType")) {
            if ("sign".equalsIgnoreCase(args[1])) {
                LockData.getAddStatus().put("sign", true);
                LockData.getAddStatus().put("door", false);
                sender.sendMessage(BasicUtil.convert(BasicUtil.replace(Lang.getString(LangType.LANG_ADDTYPE), "%type", "SIGN")));
                error = false;
            } else if ("door".equalsIgnoreCase(args[1])) {
                LockData.getAddStatus().put("door", true);
                LockData.getAddStatus().put("sign", false);
                sender.sendMessage(BasicUtil.convert(BasicUtil.replace(Lang.getString(LangType.LANG_ADDTYPE), "%type", "DOOR")));
                error = false;
            }
        }

        if (error) {
            sender.sendMessage(Lang.getString(LangType.LANG_COMMANDERROR));
        }
        return true;
    }
}
