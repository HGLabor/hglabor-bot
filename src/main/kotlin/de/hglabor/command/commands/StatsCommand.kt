package de.hglabor.command.commands

import de.hglabor.BotClient
import de.hglabor.command.SlashCommand
import de.hglabor.http.HttpUtils
import de.hglabor.logging.Level
import de.hglabor.logging.log
import dev.kord.common.Color
import dev.kord.common.annotation.KordPreview
import dev.kord.core.behavior.interaction.followUp
import dev.kord.core.entity.interaction.CommandInteraction
import dev.kord.core.entity.interaction.string
import dev.kord.rest.Image
import dev.kord.rest.builder.interaction.embed
import java.util.*

@KordPreview
object StatsCommand : SlashCommand(
    "stats",
    "Shows ingame stats on discord",
    {
        string("player", "The name of the player whose stats you want to see.") {
            required = true
        }
    }
) {

    private const val STATS_API = "https://hglabor.axay.net/api/v1/player/"
    private const val MOJANG_API = "https://api.mojang.com/users/profiles/minecraft/"
    private val random = Random()

    override suspend fun handleCommand(interaction: CommandInteraction) {
        interaction.acknowledgePublic().followUp {
            val player = interaction.command.options["player"]?.string()!!
            val jsonResponse = HttpUtils.readJsonFromUrl("$STATS_API$player")
            jsonResponse?.let {
                if(it["error"] != null) {
                    embed {
                        log(Level.ERROR, it["error"].asString)
                    }
                } else {
                    val uuid = HttpUtils.readJsonFromUrl("$MOJANG_API$player")?.get("id")
                    embed {
                        uuid?.let {
                            thumbnail {
                                url = "https://crafatar.com/avatars/${it.asString}?overlay=true"
                            }
                        }
                        title = it["name"].asString
                        description = "\n" +
                                "**Kills:** ${it["kills"].asInt}\n" +
                                "**Deaths:** ${it["deaths"].asInt}\n" +
                                "**Wins:** ${it["wins"].asInt}\n" +
                                "**Games played:** ${it["gamesPlayed"].asInt}\n"
                        color = Color(random.nextInt(255), random.nextInt(255), random.nextInt(255))
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


/*
@Serializable
data class APIResponse(
    val name: String?,
    val kills: Int,
    val deaths: Int,
    val wins: Int,
    val gamesPlayed: Int,
    val playtime: Int,
    val killRecord: Int,
    val kits: Map<String, Int>,
)

@Serializable
data class ErrorResponse(
    val error: String
)
 */