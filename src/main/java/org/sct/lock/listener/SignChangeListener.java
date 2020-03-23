package org.sct.lock.listener;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.sct.lock.data.LockData;
import org.sct.lock.enumeration.ConfigType;
import org.sct.lock.file.Config;
import org.sct.lock.util.function.SIgnProcessUtil;

/**
 * @author LovesAsuna
 * @since 2019/12/4 23:03
 */
public class SignChangeListener implements Listener {

    @EventHandler
    public void onSignChange(SignChangeEvent e) {
        Location lt = e.getBlock().getLocation();
        boolean cancel = true;

        if (LockData.getPlayerDoorLocation().get(e.getPlayer()) == null) {
            return;
        }

        for (String doors : Config.getStringList(ConfigType.SETTING_DOORTYPE)) {
            if (LockData.getPlayerDoorLocation().get(e.getPlayer()).getBlock().getType() == Material.getMaterial(doors)) {
                cancel = false;
                break;
            }
        }

        if (cancel) {
            return;
        }

        if (e.getPlayer() == LockData.getPlayerSignLocation().inverse().get(lt)) {
            SIgnProcessUtil.processSign(e);
        }
    }
}
