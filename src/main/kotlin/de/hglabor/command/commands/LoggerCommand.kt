package de.hglabor.command.commands

import de.hglabor.command.SlashCommand
import de.hglabor.guild
import de.hglabor.hasPermission
import de.hglabor.logging.DiscordLogger
import de.hglabor.logging.Level
import de.hglabor.logging.log
import de.hglabor.member
import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.Permission
import dev.kord.core.behavior.interaction.followUp
import dev.kord.core.behavior.interaction.followUpEphemeral
import dev.kord.core.entity.interaction.CommandInteraction
import dev.kord.core.entity.interaction.string
import dev.kord.rest.builder.interaction.embed

@KordPreview
object LoggerCommand : SlashCommand(
    "logger",
    "A command for admins to toggle which log levels should be displayed",
    {
        string("level", "The level to disable / enable") {
            for (loggerLevel in Level.values()) {
                choice(loggerLevel.name, loggerLevel.name)
            }
        }
    }
) {
    override suspend fun handleCommand(interaction: CommandInteraction) {
        if(interaction.member().hasPermission(Permission.ViewAuditLog)) {
            interaction.acknowledgePublic().followUp {
                interaction.command.options["level"]?.string()?.let {
                    val level = Level.valueOf(it.uppercase())
                    level.isEnabled = !level.isEnabled
                    embed {
                        log(Level.INFO, "The logger level `${level.name}` is now: **${level.isEnabled}**")
                    }
                }
            }
        } else {
            interaction.acknowledgeEphemeral().followUpEphemeral {
                embed {
                    log(Level.ERROR, "You don't have permissions to execute this command.")
                }
            }
        }
    }
}