package org.sct.lock.util.player;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.sct.lock.data.LockData;
import org.sct.lock.enumeration.ConfigType;
import org.sct.lock.file.Config;
import org.sct.plugincore.util.BasicUtil;

public class CheckUtil {

    private static Block[] FourSign;

    /**
     * @param player 交互玩家(如果单纯检测而不存进map则保持为null)
     * @param door 门
     * @return 是否为收费门牌子
     */
    public static boolean CheckSign(Player player, Block door) {
        Block aboveDoor = null;
        for (int i = 1; i <= 2; i++) {
            aboveDoor = door.getRelative(0, i, 0);
            setFourSign(aboveDoor);
            for (int j = 0; j <= 3; j++) {
                Block sign = FourSign[j];
                for (String signType : Config.getStringList(ConfigType.SETTING_SIGNTYPE)) {
                    if (sign.getType() == Material.getMaterial(signType)) {
                        if (findSign(sign)) {
                            if (player != null) {
                                int x = sign.getLocation().getBlockX();
                                int y = sign.getLocation().getBlockY();
                                int z = sign.getLocation().getBlockZ();
                                storeData(player, door, x, y, z);
                            }
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public static boolean findSign(Block sign) {
        Sign Sign = (Sign) sign.getState();
        if (Sign.getLine(0).equalsIgnoreCase(BasicUtil.convert(Config.getString(ConfigType.SETTING_SYMBOLREPLACE)))) {
            return true;
        }
        return false;
    }

    private static void storeData(Player player, Block door, int x, int y, int z) {

        //玩家交互的门(忽视高度)
        if (player != null) {
            LockData.getPlayerBlock().put(player, door);
        }

        //存入玩家交互的门上方的牌子
        if (player != null) {
            LockData.getPlayerSign().put(player, new Location(player.getWorld(), x, y, z).getBlock());
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
