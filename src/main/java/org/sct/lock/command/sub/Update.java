package org.sct.lock.command.sub;

import org.bukkit.command.CommandSender;
import org.sct.lock.Lock;
import org.sct.lock.data.LockData;
import org.sct.lock.enumeration.LangType;
import org.sct.lock.file.Lang;
import org.sct.plugincore.util.function.SubCommand;
import org.sct.plugincore.util.plugin.DownloadUtil;
import org.sct.plugincore.util.plugin.GetUpdateDetail;

import java.io.IOException;

public class Update implements SubCommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        boolean error = true;

        if (!sender.isOp()) {
            sender.sendMessage(Lang.getString(LangType.LANG_NOPERMISSION));
        }

        if (args.length == 2) {
            if (args[1].equalsIgnoreCase("download")) {
                LockData.getPool().submit(() -> {
                    try {
                        DownloadUtil.download(sender, Lock.getInstance());
                        sender.sendMessage("§7[§eLock§7]§2下载成功");
                    } catch (IOException e) {
                        sender.sendMessage("§7[§eLock§7]§c下载更新时出错");
                    }

                });
                error = false;
            } else if (args[1].equalsIgnoreCase("version")) {
                LockData.getPool().submit(() -> {
                    try {
                        GetUpdateDetail.get(sender, Lock.getInstance());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                error = false;
            }
        }

        if (error) {
            sender.sendMessage(Lang.getString(LangType.LANG_COMMANDERROR));
        }
        return true;
    }
}
