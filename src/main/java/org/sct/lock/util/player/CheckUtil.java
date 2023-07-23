package org.sct.lock.util.player;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.sct.lock.util.BasicUtil;
import org.sct.lock.data.LockData;
import org.sct.lock.enumeration.ConfigType;
import org.sct.lock.file.Config;

/**
 * @author LovesAsuna
 * @date 2020/10/19 23:14
 **/
public class CheckUtil {

    private static Block[] FourSign;

    /**
     * @param player 交互玩家(如果单纯检测而不存进map则保持为null)
     * @param door   门
     * @return 是否为收费门牌子
     */
    public static boolean checkSign(Player player, Block door) {
        Block aboveDoor;
        for (int i = 1; i <= 2; i++) {
            aboveDoor = door.getRelative(0, i, 0);
            setFourSign(aboveDoor);
            for (int j = 0; j <= 3; j++) {
                Block sign = FourSign[j];
                if (findSign(sign)) {
                    if (player != null) {
                        storeData(player, door, sign);
                    }
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean findSign(Block signBlock) {
        if (!signBlock.getType().name().contains("SIGN")) {
            return false;
        }
        Sign sign = (Sign) signBlock.getState();
        return sign.getLine(0).equalsIgnoreCase(BasicUtil.convert(Config.getString(ConfigType.SETTING_SYMBOLREPLACE.getPath())));
    }

    private static void storeData(Player player, Block door, Block sign) {
        if (player != null) {
            //玩家交互的门(忽视高度)
            LockData.INSTANCE.getPlayerDoorLocation().forcePut(player, door.getLocation());

            //存入玩家交互的门上方的牌子
            LockData.INSTANCE.getPlayerSignLocation().forcePut(player, sign.getLocation());
        }
    }

    private static void setFourSign(Block aboveDoor) {
        FourSign = new Block[]{
                aboveDoor.getRelative(1, 0, 0),
                aboveDoor.getRelative(-1, 0, 0),
                aboveDoor.getRelative(0, 0, 1),
                aboveDoor.getRelative(0, 0, -1)
        };
    }
}
