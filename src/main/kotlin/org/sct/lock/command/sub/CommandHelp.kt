package org.sct.lock.command.sub

import org.sct.lock.command.CommandExecutor
import org.sct.lock.command.CommandHandler
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.SimpleCommandBody
import taboolib.common.platform.command.subCommand
import taboolib.module.lang.sendLang

/**
 * Lock
 * org.sct.lock.command.sub.CommandHelp
 *
 * @author Mical
 * @since 2023/7/23 19:33
 */
object CommandHelp : CommandExecutor {

    override val command: SimpleCommandBody
        get() = subCommand {
            execute<ProxyCommandSender> { sender, _, _ ->
                sender.sendLang("CommandHelp")
            }
        }

    override val name: String
        get() = "help"

    override val description: String
        get() = "显示收费门创建教程"

    override val usage: String
        get() = ""

    init {
        CommandHandler.sub[name] = this
    }
}