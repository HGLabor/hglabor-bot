package de.hglabor.command

import de.hglabor.BotClient
import de.hglabor.command.commands.LoggerCommand
import de.hglabor.command.commands.RoleButtonsCommand
import de.hglabor.guild
import de.hglabor.logging.DiscordLogger
import de.hglabor.member
import dev.kord.common.annotation.KordPreview
import dev.kord.core.behavior.createApplicationCommand
import dev.kord.core.entity.Guild
import dev.kord.core.entity.interaction.CommandInteraction
import dev.kord.core.event.guild.GuildCreateEvent
import dev.kord.core.event.interaction.InteractionCreateEvent
import dev.kord.core.on
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@KordPreview
object CommandManager {

    private val commands = HashMap<String, SlashCommand>()

    fun register(command: SlashCommand) {
        commands[command.name] = command
    }

    val commandScope = CoroutineScope(Dispatchers.IO)

    suspend fun init() {
        LoggerCommand
        RoleButtonsCommand
        commandScope.launch {
            cleanupGuilds()
        }
        BotClient.client.guilds.collect {
            BotClient.logger.info("Registering commands for ${it.name}")
        }
        commandScope.launch {
            BotClient.logger.debug("Registering on guilds..")
            registerOnGuilds()
            BotClient.logger.debug("Registered on guilds..")
        }
        BotClient.client.on<GuildCreateEvent> {
            BotClient.logger.debug("Cleaning up ${guild.name}")
            this.guild.cleanupCommands()
            BotClient.logger.debug("Registering commands for ${guild.name}")
            this.guild.registerCommands()
            BotClient.logger.info("${guild.name} is ready")
        }
        BotClient.client.on<InteractionCreateEvent> {
            if(interaction is CommandInteraction) {
                commands[(interaction as CommandInteraction).command.rootName]?.handleCommand(interaction as CommandInteraction)
            }
        }
    }

    suspend fun reloadCommands() {
        commandScope.launch {
            cleanupGuilds()
            registerOnGuilds()
        }
    }

    private suspend fun registerOnGuilds() = BotClient.client.guilds.collect { it.registerCommands() }

    private suspend fun cleanupGuilds() = BotClient.client.guilds.collect { it.cleanupCommands() }

    private suspend fun Guild.registerCommands() {
        CommandManager.commands.forEach { commandEntry ->
            val command = commandEntry.value
            createApplicationCommand(command.name, command.description) { command.builder.invoke(this) }
        }
    }

    private suspend fun Guild.cleanupCommands() {
        commands.collect { command ->
            if (!CommandManager.commands.containsKey(command.name))
                command.delete()
        }
    }

}