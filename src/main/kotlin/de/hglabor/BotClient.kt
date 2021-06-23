package de.hglabor

import com.gitlab.kordlib.kordx.emoji.Emojis.label
import de.hglabor.command.CommandManager
import de.hglabor.config.ConfigManager
import de.hglabor.database.MongoManager
import de.hglabor.listener.ButtonListener
import de.hglabor.logging.Logger
import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.*
import dev.kord.core.Kord
import dev.kord.core.behavior.channel.MessageChannelBehavior
import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.entity.Guild
import dev.kord.core.entity.Member
import dev.kord.core.entity.interaction.Interaction
import dev.kord.rest.builder.component.ActionRowBuilder
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@KordPreview
suspend fun main() {
    BotClient.start()
}



object BotClient {

    lateinit var client: Kord; private set

    lateinit var hgLaborGuild: Guild

    val logger = Logger(System.out)

    @KordPreview
    suspend fun start() {
        logger.info("Starting Bot on ${System.getProperty("os.name")} ${System.getProperty("os.version")} (Java ${System.getProperty("java.runtime.version")})")
        client = Kord(
            ConfigManager.discordSettings.token
                ?: error("Configure the application before running it"))
        hgLaborGuild = client.getGuild(Snowflake(ConfigManager.discordSettings.guildId!!))!!
        CommandManager.init()
        //REGISTER LISTENER
        ButtonListener
        client.login()
    }
}


@KordPreview
suspend fun Interaction.guild(): Guild {
    return kord.getGuild(data.guildId.value!!)!!
}

@KordPreview
suspend fun Interaction.member(): Member  {
    return guild().getMember(data.member.value!!.userId)
}

@KordPreview
suspend fun Member.hasPermission(permission: Permission): Boolean {
    return getPermissions().contains(permission)
}

@KordPreview
suspend fun Member.hasRole(id: Snowflake): Boolean {
    var returnValue = false
    for(it in roles.toList()) {
        if(it.id == id) {
            returnValue = true
        }
    }
    return returnValue
}

@KordPreview
suspend fun MessageChannelBehavior.createRoleButton(buttonLabel: String, id: String, roleId: String) {
    val roleButton = RoleButton(
        id,
        roleId
    )
    MongoManager.roleButtons.insertOne(roleButton)
    createMessage {
        content = "Click this button to get the ${BotClient.hgLaborGuild.getRole(Snowflake(roleId)).mention} role"
        actionRow {
            interactionButton(
                style = ButtonStyle.Primary,
                customId = id
            ) {
                label = buttonLabel
            }
        }
    }
}

@Serializable
data class RoleButton(@SerialName("_id")val id: String, val roleId: String)


