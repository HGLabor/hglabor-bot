package de.hglabor.config

import com.typesafe.config.ConfigFactory
import de.hglabor.config.data.DatabaseSettings
import de.hglabor.config.data.DiscordApplication
import de.hglabor.config.data.TwitchApplication
import io.github.config4k.extract
import java.io.File

object ConfigManager {

    class ConfigFile(path: String) : File(path) {
        init {
            if (!parentFile.exists()) parentFile.mkdirs()
            if (!exists()) createNewFile()
        }
    }

    val discordSettings = ConfigFactory.parseFile(ConfigFile("./settings/discord.settings")).extract<DiscordApplication>()

    val databaseSettings = ConfigFactory.parseFile(ConfigFile("./settings/mongodb.settings")).extract<DatabaseSettings>()

    val twitchSettings = ConfigFactory.parseFile(ConfigFile("./settings/twitch.settings")).extract<TwitchApplication>()

}