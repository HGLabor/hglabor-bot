package de.hglabor.command.commands

import de.hglabor.*
import de.hglabor.command.SlashCommand
import de.hglabor.listener.ButtonListener
import de.hglabor.logging.DiscordLogger
import de.hglabor.logging.Level
import de.hglabor.logging.log
import dev.kord.common.Color
import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.Permission
import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.channel.MessageChannelBehavior
import dev.kord.core.behavior.edit
import dev.kord.core.behavior.interaction.followUp
import dev.kord.core.behavior.interaction.followUpEphemeral
import dev.kord.core.entity.Guild
import dev.kord.core.entity.Member
import dev.kord.core.entity.interaction.CommandInteraction
import dev.kord.core.entity.interaction.string
import dev.kord.rest.builder.interaction.embed
import kotlinx.coroutines.delay
import java.util.*

@KordPreview
object RoleButtonsCommand : SlashCommand(
    "rolebutton",
    " //Create a button to get a role when pressing it",
    {
        //subCommand("create", "Create a new button") {
            string("channelid", "The ID of the channel where the button should be created") {
                required = true
            }
            string("roleid", "The ID of the role to give") {
                required = true
            }
        //}
    }
) {
    override suspend fun handleCommand(interaction: CommandInteraction) {
        val logger = DiscordLogger(interaction.channel, interaction.guild())
        if(!interaction.member().hasPermission(Permission.ManageRoles)) {
            interaction.acknowledgeEphemeral().followUpEphemeral {
                embed {
                    log(Level.ERROR, "You don't have permission to execute this command.")
                }
            }
            return
        }
        val channelId = interaction.command.options["channelid"]?.string()
        val roleId = interaction.command.options["roleid"]?.string()
        if(channelId != null && roleId != null) {
            interaction.acknowledgePublic().followUp {
                val channel = BotClient.hgLaborGuild.getChannel(Snowflake(channelId)) as MessageChannelBehavior
                val role = BotClient.hgLaborGuild.getRole(Snowflake(roleId))
                channel.createRoleButton("Get ${role.name}", "role${roleId}${Random().nextInt(2555)}", roleId)
                delay(1000)
                ButtonListener.registerButtons()
                embed {
                    log(Level.INFO, "Created the button.")
                }
            }
        }
    }
}