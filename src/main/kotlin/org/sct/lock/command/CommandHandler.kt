/*
 *  Copyright (C) <2023>  <Mical>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.sct.lock.command

import org.bukkit.command.CommandSender
import org.sct.lock.command.sub.CommandHelp
import org.sct.lock.prettyInfo
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.component.CommandBase
import taboolib.common.platform.command.component.CommandComponent
import taboolib.common.platform.command.component.CommandComponentLiteral
import taboolib.common.platform.command.mainCommand
import taboolib.common.platform.function.adaptCommandSender
import taboolib.common.platform.function.pluginVersion
import taboolib.common.util.Strings
import taboolib.module.chat.RawMessage
import taboolib.module.chat.colored
import taboolib.module.nms.MinecraftVersion
import java.util.concurrent.ConcurrentHashMap

/**
 * AlgaeNameTag
 * me.xiaozhangup.algae.command.CommandHandler
 *
 * @author Mical
 * @since 2023/7/12 00:20
 */
@CommandHeader(name = "lock", permission = "lock.use")
object CommandHandler {

    val sub = ConcurrentHashMap<String, CommandExecutor>()

    @CommandBody(permission = "lock.use")
    val main = mainCommand {
        createTabooHelper()
    }

    @CommandBody(permission = "lock.use")
    val help = CommandHelp.command

    private fun CommandComponent.createTabooHelper() {
        execute<CommandSender> { sender, context, _ ->
            sender.sendMessage("")
            RawMessage()
                .append("  ").append("&{#EB5335}Lock".colored())
                .hoverText("§7自动收费门")
                .append(" ").append("§f${pluginVersion}")
                .hoverText(
                    """
                §7插件版本: §2${pluginVersion}
                §7游戏版本: §b${MinecraftVersion.minecraftVersion}
            """.trimIndent()
                ).sendTo(adaptCommandSender(sender))
            sender.sendMessage("")
            RawMessage()
                .append("  §7命令: ").append("§f/lock §8[...]")
                .hoverText("§f/lock §8[...]")
                .suggestCommand("/lock ")
                .sendTo(adaptCommandSender(sender))
            sender.sendMessage("  §7参数:")


            for (command in children.filterIsInstance<CommandComponentLiteral>()) {
                if (!sender.isOp) {
                    if (!sender.hasPermission(command.permission)) {
                        continue
                    } else {
                        if (command.hidden) continue
                    }
                }
                val name = command.aliases[0]
                var usage = sub[name]?.usage ?: ""
                if (usage.isNotEmpty()) {
                    usage += " "
                }
                val description = sub[name]?.description ?: "没有描述"

                RawMessage()
                    .append("    §8- ").append("§f$name")
                    .hoverText("§f/lock $name $usage§8- §7$description")
                    .suggestCommand("/lock $name ")
                    .sendTo(adaptCommandSender(sender))
                sender.sendMessage("      §7$description")
            }

            sender.sendMessage("")
        }

        if (this is CommandBase) {
            incorrectCommand { sender, ctx, _, state ->

                val input = ctx.args().first()
                val name = children.filterIsInstance<CommandComponentLiteral>().firstOrNull { it.aliases.contains(input) }?.aliases?.get(0) ?: input
                var usage = sub[name]?.usage ?: ""
                if (usage.isNotEmpty()) {
                    usage += " "
                }
                val description = sub[name]?.description ?: "没有描述"
                when (state) {
                    1 -> {
                        sender.prettyInfo("指令 §f{0} §7参数不足.", name)
                        sender.prettyInfo("正确用法:")
                        sender.prettyInfo("§f/lock {0} {1}§8- §7{2}", name, usage, description)
                    }

                    2 -> {
                        if (ctx.args().size > 1) {
                            sender.prettyInfo("指令 §f{0} §7参数不足.", name)
                            sender.prettyInfo("正确用法:")
                            sender.prettyInfo("§f/lock {0} {1}§8- §7{2}", name, usage, description)
                        } else {
                            val similar = sub.keys
                                .asSequence()
                                .map { children.filterIsInstance<CommandComponentLiteral>().firstOrNull { c -> c.aliases[0] == it } }
                                .filterNotNull()
                                .filterNot { it.hidden }
                                .filter { sender.hasPermission(it.permission) }
                                .maxByOrNull { Strings.similarDegree(name, it.aliases[0]) }!!
                                .aliases[0]
                            sender.prettyInfo("指令 §f{0} §7不存在.", name)
                            sender.prettyInfo("你可能想要:")
                            sender.prettyInfo(similar)
                        }
                    }
                }
            }
            incorrectSender { sender, ctx ->
                sender.prettyInfo("指令 §f{0} §7只能由 §f玩家 §7执行", ctx.args().first())
            }
        }
    }
}