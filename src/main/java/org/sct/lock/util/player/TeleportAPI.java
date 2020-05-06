package org.sct.lock.util.player;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.sct.lock.data.LockData;


/**
 * @author LovesAsuna
 */
public class TeleportAPI {
    private Block sign, block;
    private double PlayerX, PlayerY, PlayerZ, BlockX, BlockZ;
    private String blockFace;
    private final String N = "north";
    private final String W = "west";
    private final String E = "east";
    private final String S = "south";

    /**
     * @param player 被tp的玩家(payer)
     * @return 进出状态
     */
    public status getPlayerFace(Player player) {
        /*判断进出状态*/
        status s = null;
        if (blockFace.equalsIgnoreCase(N)) {
            if (PlayerZ < BlockZ) {
                s = status.ENTER;
            } else {
                s = status.LEAVE;
            }
        } else if (blockFace.equalsIgnoreCase(S)) {
            if (PlayerZ > BlockZ) {
                s = status.ENTER;
            } else {
                s = status.LEAVE;
            }
        } else if (blockFace.equalsIgnoreCase(W)) {
            if (PlayerX < BlockX) {
                s = status.ENTER;
            } else {
                s = status.LEAVE;
            }
        } else if (blockFace.equalsIgnoreCase(E)) {
            if (PlayerX > BlockX) {
                s = status.ENTER;
            } else {
                s = status.LEAVE;
            }
        }
        return s;
    }

    public static enum status {
        ENTER,
        LEAVE,
        DOUBLE;
    }

    public void Tp(status s, Player player) {
        /*传送部分*/
        switch (s) {
            case ENTER:
                if (blockFace.equalsIgnoreCase(N)) {
                    BlockZ += 1.5;
                    BlockX += 0.5;
                }
                if (blockFace.equalsIgnoreCase(S)) {
                    BlockZ -= 0.5;
                    BlockX += 0.5;
                }
                if (blockFace.equalsIgnoreCase(W)) {
                    BlockX += 1.5;
                    BlockZ += 0.5;
                }
                if (blockFace.equalsIgnoreCase(E)) {
                    BlockX -= 0.5;
                    BlockZ += 0.5;
                }
                player.teleport(new Location(player.getWorld(), BlockX, PlayerY, BlockZ, player.getLocation().getYaw(), player.getLocation().getPitch()));
                break;
            case LEAVE:
                if (blockFace.equalsIgnoreCase(N)) {
                    BlockZ -= 0.5;
                    BlockX += 0.5;
                }
                if (blockFace.equalsIgnoreCase(S)) {
                    BlockZ += 1.5;
                    BlockX += 0.5;
                }
                if (blockFace.equalsIgnoreCase(W)) {
                    BlockX -= 0.5;
                    BlockZ += 0.5;
                }
                if (blockFace.equalsIgnoreCase(E)) {
                    BlockX += 1.5;
                    BlockZ += 0.5;
                }
                player.teleport(new Location(player.getWorld(), BlockX, PlayerY, BlockZ, player.getLocation().getYaw(), player.getLocation().getPitch()));
                break;
        }
    }

    public void getData(Player player) {
        sign = LockData.INSTANCE.getPlayerSign().get(player);
        block = LockData.INSTANCE.getPlayerBlock().get(player);

        if (getBlockFace(sign.getRelative(0, 0, 1))) {
            blockFace = N;
        } else if (getBlockFace(sign.getRelative(0, 0, -1))) {
            blockFace = S;
        } else if (getBlockFace(sign.getRelative(1, 0, 0))) {
            blockFace = W;
        } else if (getBlockFace(sign.getRelative(-1, 0, 0))) {
            blockFace = E;
        }

        PlayerX = player.getLocation().getBlockX();
        PlayerY = player.getLocation().getBlockY();
        PlayerZ = player.getLocation().getBlockZ();
        BlockX = block.getLocation().getBlockX();
        BlockZ = block.getLocation().getBlockZ();
    }

    private boolean getBlockFace(Block signRelative) {
        if (signRelative.getType() == Material.AIR) {
            return false;
        }

        if (signRelative.getType() == block.getRelative(0, 1, 0).getType() || signRelative.getType() == block.getRelative(0, 2, 0).getType()) {
            return true;
        } else {
            return false;
        }
    }

}
