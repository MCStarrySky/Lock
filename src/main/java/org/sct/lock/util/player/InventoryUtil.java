package org.sct.lock.util.player;

import org.apache.commons.lang.ArrayUtils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Arrays;

/**
 * @author alchemy
 * @date 2020-02-07 12:31
 */
public class InventoryUtil {

    /**
     * 判断指定玩家背包是否为空
     *
     * @param player 指定玩家
     * @return true为空，false为不空
     */
    public static boolean isInvEmpty(Player player) {
        PlayerInventory inventory = player.getInventory();
        ItemStack[] checkList = inventory.getContents();

        for (ItemStack item : checkList) {
            if (item != null) {
                return false;
            }
        }
        return true;
    }
}
