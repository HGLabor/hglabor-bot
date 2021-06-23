package de.hglabor.logging

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
        var returned: String
        returned = log(Level.FATAL, message)
        exception.printStackTrace()
        return returned
    }

    fun log(level: Level, message: String): String {
        out.println("[${Thread.currentThread()}/${level.name}]: $message")
        return message
    }

}

@KordPreview
class DiscordLogger(val out: MessageChannelBehavior, val guild: Guild) {

    suspend fun debug(message: String): Message {
        return log(Level.DEBUG, message)
    }

    suspend fun info(message: String): Message {
        return log(Level.INFO, message)
    }

    suspend fun warn(message: String): Message {
        return log(Level.WARN, message)
    }

    suspend fun error(message: String): Message {
        return log(Level.ERROR, message)
    }

    suspend fun fatal(message: String): Message {
        return log(Level.FATAL, message)
    }
    suspend fun fatal(message: String, exception: Exception): Message {
        val returned: Message = log(Level.FATAL, message)
        exception.printStackTrace()
        return returned
    }

    suspend fun log(level: Level, message: String): Message {
        return out.createEmbed {
            title = message
            color = Color(level.decimalColor)
            val foot = EmbedBuilder.Footer()
            foot.icon = guild.getIconUrl(Image.Format.GIF)!!
            foot.text = guild.name
        }
    }

}