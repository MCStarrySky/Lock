package org.sct.lock

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.command.CommandSender
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.function.console
import taboolib.common.util.replaceWithOrder
import taboolib.common5.cdouble
import taboolib.module.lang.asLangText
import taboolib.platform.util.asLangText

/**
 * Lock
 * org.sct.lock.Utils
 *
 * @author Mical
 * @since 2023/7/23 19:24
 */
fun ProxyCommandSender.prettyInfo(message: String, vararg args: Any) {
    sendMessage("${asLangText("prefix")} §7" + message.replaceWithOrder(*args))
}

fun CommandSender.prettyInfo(message: String, vararg args: Any) {
    sendMessage("${asLangText("prefix")} §7" + message.replaceWithOrder(*args))
}

fun prettyInfo(message: String, vararg args: Any) {
    console().prettyInfo(message, args)
}

fun Location.parseToString(): String = "${world?.name},$blockX,$blockY,$blockZ"

fun String.parseToLocation(): Location {
    val world = split(",", limit = 4)[0]
    val (x, y, z) = split(",", limit = 4).drop(1).map { it.toDouble() }
    return Location(Bukkit.getWorld(world), x, y, z)
}

fun Location.toRoundedLocation(): Location = Location(world, blockX.cdouble, blockY.cdouble, blockZ.cdouble)