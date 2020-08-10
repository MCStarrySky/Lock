package org.sct.lock.event;

import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.sct.lock.util.player.TeleportHandler;

/**
 * @author alchemy
 * @since 2019/12/9/17:13:13
 */
public class PlayerAccessLockDoorEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    Player payer;
    OfflinePlayer owner;
    Block block;
    TeleportHandler teleportHandler;

    /**
     * 构造函数
     *
     * @param payer 支付费用使用收费门的玩家
     * @param owner 收费门的所有者
     * @param block 收费门上面的木牌，用于判断信息
     */
    public PlayerAccessLockDoorEvent(Player payer, OfflinePlayer owner, TeleportHandler teleportHandler, Block block) {
        this.payer = payer;
        this.owner = owner;
        this.teleportHandler = teleportHandler;
        this.block = block;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Block getBlock() {
        return block;
    }

    public OfflinePlayer getOwner() {
        return owner;
    }

    public Player getPayer() {
        return payer;
    }

    public TeleportHandler getTeleportAPI() {
        return teleportHandler;
    }
}
