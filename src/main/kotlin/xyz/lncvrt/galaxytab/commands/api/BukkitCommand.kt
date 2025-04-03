package xyz.lncvrt.galaxytab.commands.api

import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import org.incendo.cloud.SenderMapper
import org.incendo.cloud.description.Description
import org.incendo.cloud.execution.ExecutionCoordinator
import org.incendo.cloud.kotlin.MutableCommandBuilder
import org.incendo.cloud.kotlin.extension.buildAndRegister
import org.incendo.cloud.kotlin.extension.commandBuilder
import org.incendo.cloud.paper.LegacyPaperCommandManager
import org.incendo.cloud.setting.ManagerSetting

abstract class BukkitCommand(
    plugin: JavaPlugin,
    private val name: String,
    private val aliases: Array<String>
) {
    private val manager = LegacyPaperCommandManager(
        plugin,
        ExecutionCoordinator.simpleCoordinator(),
        SenderMapper.create({ commandSender: CommandSender -> BukkitSender(commandSender) }, { bukkitSender: BukkitSender -> bukkitSender.platformSender() })
    )
    private val command: MutableCommandBuilder<BukkitSender>

    init {
        manager.settings().set(ManagerSetting.OVERRIDE_EXISTING_COMMANDS, true)
//        manager.registerAsynchronousCompletions()

        command = manager.buildAndRegister(name, Description.empty(), aliases) {
            rootBuilder(this)
        }
    }

    fun rawCommandBuilder() = manager.commandBuilder(name, Description.empty(), aliases) { }

    open fun rootBuilder(builder: MutableCommandBuilder<BukkitSender>) { }
}