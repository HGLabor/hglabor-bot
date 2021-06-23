package de.hglabor.logging

import de.hglabor.BotClient
import dev.kord.common.Color
import dev.kord.common.annotation.KordPreview
import dev.kord.core.behavior.channel.MessageChannelBehavior
import dev.kord.core.behavior.channel.createEmbed
import dev.kord.core.entity.Guild
import dev.kord.core.entity.Message
import dev.kord.rest.Image
import dev.kord.rest.builder.message.EmbedBuilder
import java.io.PrintStream
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class Logger(val out: PrintStream) {

    fun debug(message: String): String {
        return log(Level.DEBUG, message)
    }

    fun info(message: String): String {
        return log(Level.INFO, message)
    }

    fun warn(message: String): String {
        return log(Level.WARN, message)
    }

    fun error(message: String): String {
        return log(Level.ERROR, message)
    }

    fun fatal(message: String): String {
        return log(Level.FATAL, message)
    }
    fun fatal(message: String, exception: Exception): String {
        val returned = log(Level.FATAL, message)
        exception.printStackTrace()
        return returned
    }

    fun log(level: Level, message: String): String {
        if(level.isEnabled) {
            out.println("[${SimpleDateFormat("hh:MM:ss").format(Date())}] [${Thread.currentThread().threadGroup.name}/${level.name}]: $message")
            return message
        } else {
            return "The logger level ${level.name} is disabled"
        }
    }

}

@KordPreview
class DiscordLogger(val out: MessageChannelBehavior, val guild: Guild) {

    suspend fun debug(message: String) {
        log(Level.DEBUG, message)
    }

    suspend fun info(message: String) {
        log(Level.INFO, message)
    }

    suspend fun warn(message: String) {
        log(Level.WARN, message)
    }

    suspend fun error(message: String) {
        log(Level.ERROR, message)
    }

    suspend fun fatal(message: String) {
         log(Level.FATAL, message)
    }
    suspend fun fatal(message: String, exception: Exception) {
        log(Level.FATAL, message)
        exception.printStackTrace()
    }

    suspend fun log(level: Level, message: String) {
        if(level.isEnabled) {
            out.createEmbed {
                title = message
                color = Color(level.decimalColor)
                val foot = EmbedBuilder.Footer()
                foot.icon = guild.getIconUrl(Image.Format.GIF)!!
                foot.text = guild.name
                footer = foot
            }
        }
    }

}

fun EmbedBuilder.log(level: Level, message: String) {
    if(level.isEnabled) {
        title = message
        color = Color(level.decimalColor)
        val foot = EmbedBuilder.Footer()
        foot.icon = BotClient.hgLaborGuild.getIconUrl(Image.Format.GIF)!!
        foot.text = BotClient.hgLaborGuild.name
        footer = foot
    } else {
        title = "The logger level ${level.name} is disabled"
        color = Color(Level.FATAL.decimalColor)
        val foot = EmbedBuilder.Footer()
        foot.icon = BotClient.hgLaborGuild.getIconUrl(Image.Format.GIF)!!
        foot.text = BotClient.hgLaborGuild.name
        footer = foot
    }
}