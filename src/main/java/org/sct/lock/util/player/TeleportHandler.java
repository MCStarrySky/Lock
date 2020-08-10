package org.sct.lock.util.player;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.sct.lock.data.LockData;
import org.sct.lock.enumeration.Direction;

/**
 * @author LovesAsuna
 */
public class TeleportHandler {
    private Block block;
    private double playerX, playerY, playerZ, blockX, blockZ;
    private BlockFace blockFace;

    /**
     * @param player 被tp的玩家(payer)
     * @return 进出门方向
     */
    public Direction getPlayerDirection(Player player) {
        /*判断进出状态*/
        Direction direction = null;
        switch (blockFace) {
            case NORTH:
                if (playerZ < blockZ) {
                    direction = Direction.ENTER;
                } else {
                    direction = Direction.LEAVE;
                }
                break;
            case SOUTH:
                if (playerZ > blockZ) {
                    direction = Direction.ENTER;
                } else {
                    direction = Direction.LEAVE;
                }
                break;
            case WEST:
                if (playerX < blockX) {
                    direction = Direction.ENTER;
                } else {
                    direction = Direction.LEAVE;
                }
                break;
            case EAST:
                if (playerX > blockX) {
                    direction = Direction.ENTER;
                } else {
                    direction = Direction.LEAVE;
                }
                break;
            default:
        }
        return direction;
    }

    /**
     * @param direction 进出门方向
     * @param player 玩家
     **/
    public void tp(Direction direction, Player player) {
        /*传送部分*/
        switch (direction) {
            case ENTER:
                switch (blockFace){
                    case NORTH:
                        blockZ += 1.5;
                        blockX += 0.5;
                        break;
                    case SOUTH:
                        blockZ -= 0.5;
                        blockX += 0.5;
                        break;
                    case WEST:
                        blockX += 1.5;
                        blockZ += 0.5;
                        break;
                    case EAST:
                        blockX -= 0.5;
                        blockZ += 0.5;
                        break;
                    default:
                }
                player.teleport(new Location(player.getWorld(), blockX, playerY, blockZ, player.getLocation().getYaw(), player.getLocation().getPitch()));
                break;
            case LEAVE:
                switch (blockFace) {
                    case NORTH:
                        blockZ -= 0.5;
                        blockX += 0.5;
                        break;
                    case SOUTH:
                        blockZ += 1.5;
                        blockX += 0.5;
                        break;
                    case WEST:
                        blockX -= 0.5;
                        blockZ += 0.5;
                        break;
                    case EAST:
                        blockX += 1.5;
                        blockZ += 0.5;
                        break;
                    default:
                }
                player.teleport(new Location(player.getWorld(), blockX, playerY, blockZ, player.getLocation().getYaw(), player.getLocation().getPitch()));
                break;
            default:
        }
    }

    /**
     * @param player 玩家
     * @return 此对象
     **/
    public TeleportHandler setData(Player player) {
        Block sign = LockData.INSTANCE.getPlayerSignLocation().get(player).getBlock();
        block = LockData.INSTANCE.getPlayerDoorLocation().get(player).getBlock();

        if (getBlockFace(sign.getRelative(0, 0, 1))) {
            blockFace = BlockFace.NORTH;
        } else if (getBlockFace(sign.getRelative(0, 0, -1))) {
            blockFace = BlockFace.SOUTH;
        } else if (getBlockFace(sign.getRelative(1, 0, 0))) {
            blockFace = BlockFace.WEST;
        } else if (getBlockFace(sign.getRelative(-1, 0, 0))) {
            blockFace = BlockFace.EAST;
        }

        playerX = player.getLocation().getBlockX();
        playerY = player.getLocation().getBlockY();
        playerZ = player.getLocation().getBlockZ();
        blockX = block.getLocation().getBlockX();
        blockZ = block.getLocation().getBlockZ();
        return this;
    }

    private boolean getBlockFace(Block signRelative) {
        if (signRelative.getType() == Material.AIR) {
            return false;
        }

        return signRelative.getType() == block.getRelative(0, 1, 0).getType() ||
                signRelative.getType() == block.getRelative(0, 2, 0).getType();
    }

}
