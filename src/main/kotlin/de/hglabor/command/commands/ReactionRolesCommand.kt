package de.hglabor.command.commands

import com.gitlab.kordlib.kordx.emoji.Emojis
import de.hglabor.BotClient
import de.hglabor.command.SlashCommand
import de.hglabor.hasPermission
import de.hglabor.listener.ButtonListener
import de.hglabor.logging.DiscordLogger
import de.hglabor.logging.Level
import de.hglabor.member
import dev.kord.common.Color
import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.Permission
import dev.kord.common.entity.Permissions
import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.interaction.followUp
import dev.kord.core.behavior.interaction.followUpEphemeral
import dev.kord.core.entity.Guild
import dev.kord.core.entity.Member
import dev.kord.core.entity.interaction.CommandInteraction
import dev.kord.core.entity.interaction.Interaction
import dev.kord.core.entity.interaction.string
import dev.kord.rest.builder.interaction.embed
import kotlin.reflect.jvm.javaField

@KordPreview
object ReactionRolesCommand : SlashCommand(
    name = "reactionrole",
    description = "Create a reaction to get a role when reacting with it",
    {
        subCommand("create", "Create a new reaction") {
            string("messageId", "The ID of the message where the reaction should be added") {
                required = true
            }
            string("roleId", "The ID of the role to give") {
                required = true
            }
            string("emote", "The emote used for the reaction") {
                required = true
                for (it in Emojis::class.java.fields) {
                    choice(it.name, "A vanilla discord emoji")
                }
            }
        }
    }
) {
    override suspend fun handleCommand(interaction: CommandInteraction, logger: DiscordLogger, member: Member, guild: Guild) {
        if(!member.hasPermission(Permission.ManageRoles)) {
            interaction.acknowledgeEphemeral().followUpEphemeral {
                embed {
                    color = Color(Level.ERROR.decimalColor)
                    title = "You don't have permission to use this command."
                }
            }
            return
        }
        val messageId = interaction.command.options["messageId"]?.string()
        val roleId = interaction.command.options["roleId"]?.string()
        val emote = interaction.command.options["emote"]?.string()
        if(messageId != null && roleId != null && emote !=null) {
            interaction.acknowledgePublic().followUp {
                ButtonListener.registerButtons()
            }
        }
    }
}