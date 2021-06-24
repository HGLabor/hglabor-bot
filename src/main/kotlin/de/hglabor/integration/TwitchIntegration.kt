package de.hglabor.integration

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential
import com.github.twitch4j.TwitchClient
import com.github.twitch4j.TwitchClientBuilder
import com.github.twitch4j.events.ChannelGoLiveEvent
import com.github.twitch4j.events.ChannelGoOfflineEvent
import de.hglabor.BotClient
import de.hglabor.config.ConfigManager
import dev.kord.common.Color
import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.channel.MessageChannelBehavior
import dev.kord.core.behavior.channel.createEmbed
import dev.kord.rest.Image

object TwitchIntegration {

    lateinit var twitchClient: TwitchClient

    @KordPreview
    fun enable() {
        var sentMessage: Snowflake? = null
        //val credential = OAuth2Credential("twitch", ConfigManager.twitchSettings.token ?: BotClient.logger.fatal("Twitch credential cannot be null", NullPointerException()))
        twitchClient = TwitchClientBuilder.builder()
            .withEnableHelix(true)
            .withClientId(ConfigManager.twitchSettings.clientId ?: BotClient.logger.fatal("Twitch credential cannot be null", NullPointerException()))
            //.withChatAccount(credential)
            .withEnableKraken(true)
            //.withDefaultAuthToken(credential)
            .withClientSecret(ConfigManager.twitchSettings.clientSecret ?: BotClient.logger.fatal("Twitch credential cannot be null", NullPointerException()))
            .withEnableChat(true)
            .build()
        twitchClient.eventManager.onEvent(ChannelGoOfflineEvent::class.java) {
            suspend {
                val mediaChannel = BotClient.hgLaborGuild.getChannel(Snowflake(ConfigManager.discordSettings.mediaChannelId ?: BotClient.logger.error("Discord application cannot be null"))) as MessageChannelBehavior
                if(sentMessage != null) {
                    mediaChannel.getMessage(sentMessage!!).delete()
                    sentMessage = null
                }
            }
        }
        twitchClient.eventManager.onEvent(ChannelGoLiveEvent::class.java) {
            suspend {
                val twitchPingRole = BotClient.hgLaborGuild.getRole(Snowflake(ConfigManager.discordSettings.twitchPingRole ?: BotClient.logger.error("Discord application cannot be null")))
                val mediaChannel = BotClient.hgLaborGuild.getChannel(Snowflake(ConfigManager.discordSettings.mediaChannelId ?: BotClient.logger.error("Discord application cannot be null"))) as MessageChannelBehavior
                if(sentMessage == null) {
                    mediaChannel.createMessage("${twitchPingRole.mention} ${it.channel.name} is now live!")
                    mediaChannel.createEmbed {
                        color = Color(9520895)
                        title = it.channel.name
                        description = "[${it.stream.title}](https://www.twitch.tv/${it.channel.name})"
                        url = "https://www.twitch.tv/${it.channel.name}"
                        image = it.stream.thumbnailUrl
                        thumbnail {
                            url = BotClient.hgLaborGuild.getIconUrl(Image.Format.GIF)!!
                        }
                        footer {
                            icon = BotClient.hgLaborGuild.getIconUrl(Image.Format.GIF)!!
                            text = BotClient.hgLaborGuild.name
                        }
                    }
                }
            }
        }
    }

}