package de.hglabor.config.data

data class DiscordApplication(
    val token: String? = null,
    val guildId: String? = null,
    val mediaChannelId: String? = null,
    val twitchPingRole: String? = null
)
