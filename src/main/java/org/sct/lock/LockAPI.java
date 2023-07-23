package org.sct.lock;

import org.bukkit.NamespacedKey;
import taboolib.platform.util.BukkitPluginKt;

/**
 * Lock
 * org.sct.lock.LockAPI
 *
 * @author Mical
 * @since 2023/7/23 20:15
 */
public class LockAPI {

    public static final NamespacedKey LOCK = new NamespacedKey(BukkitPluginKt.getBukkitPlugin(), "lock");

    public static final NamespacedKey LOCKCONDITION = new NamespacedKey(BukkitPluginKt.getBukkitPlugin(), "lockcondition");

    public static final NamespacedKey LOCKUSER = new NamespacedKey(BukkitPluginKt.getBukkitPlugin(), "lockuser");

    public static final NamespacedKey LOCATION = new NamespacedKey(BukkitPluginKt.getBukkitPlugin(), "locations");
}
