package de.hglabor

import de.hglabor.config.ConfigManager
import dev.kord.common.annotation.KordPreview
import dev.kord.core.Kord
import dev.kord.core.entity.Guild
import dev.kord.core.entity.Member
import dev.kord.core.entity.interaction.Interaction

@KordPreview
suspend fun main() {
    BotClient.start()
}

object BotClient {

    lateinit var client: Kord; private set

    @KordPreview
    suspend fun start() {
        println("Starting...")
        client = Kord(
            ConfigManager.discordSettings.token
                ?: error("Configure the application before running it"))
        //TODO CommandManager.init()
        //REGISTER LISTENER
        client.login()
    }
}

/*
object KordEXT {

    //some weird bug appeared, this was a notlösung

    @KordPreview
    suspend val Interaction.guild: Guild
    get() = kord.getGuild(data.guildId.value!!)!!


    @KordPreview
    val Interaction.member: Member
    get() = guild.getMember(data.member.value!!.userId)


}
 */