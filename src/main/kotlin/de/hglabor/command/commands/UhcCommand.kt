package de.hglabor.command.commands

import com.gitlab.kordlib.kordx.emoji.Emojis
import de.hglabor.BotClient
import de.hglabor.command.SlashCommand
import de.hglabor.logging.Level
import de.hglabor.logging.log
import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.Permission
import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.interaction.followUp
import dev.kord.core.behavior.interaction.followUpEphemeral
import dev.kord.core.entity.interaction.CommandInteraction
import dev.kord.core.entity.interaction.string
import dev.kord.rest.builder.interaction.embed


@KordPreview
object UhcCommand : SlashCommand(
    "uhc",
    "Auto-generate an UHC event",
    {

        string("host", "Host des UHC's")
        string("hostmedia", "Media link zum stream des hosts")
        string("time", "Start-zeit")
        string("teamsize", "Teamgröße")

    }
) {
    override suspend fun handleCommand(interaction: CommandInteraction) {
        val isAdmin = interaction.user.asMemberOrNull(interaction.data.guildId.value ?: return)?.getPermissions()?.contains(Permission.Administrator) == true
        if (isAdmin) {
            val host = interaction.command.options["host"]?.string().orEmpty()
            val time = interaction.command.options["time"]?.string().orEmpty()
            val hostmedia = interaction.command.options["hostmedia"]?.string().orEmpty()
            val teamsize = interaction.command.options["teamsize"]?.string().orEmpty()
            interaction.acknowledgePublic().followUp {
                content = "${Emojis.comet} **UHC EVENT** \n" +
                        "\n" +
                        "${Emojis.door} *knock knock...* \n" +
                        "${Emojis.faceWithRaisedEyebrow} who's there? \n" +
                        "\n" +
                        "Another HGLabor UHC Event. \n" +
                        "HGLabor is going to host **ANOTHER** UHC Event. \n" +
                        "If you want to play the Event make sure to... \n" +
                        "\n" +
                        "Join: `uhc.hglabor.de` ${Emojis.testTube} at `${time}` ${Emojis.clock1} \n" +
                        "\n" +
                        "This **UHC** is getting hosted by: `${host}` *(${hostmedia})* ${Emojis.manTechnologist} \n" +
                        "Team-size: `${teamsize}` ${Emojis.electricPlug} \n" +
                        "\n" +
                        "*Thanks ${host} for hosting this UHC!* \n" +
                        "\n" +
                        "I hope we see us on there ${Emojis.relaxed} \n" +
                        "GL&HF ${Emojis.fourLeafClover}\n" +
                        "\n" +
                        "${BotClient.hgLaborGuild.getRole(Snowflake("857590545562861588")).mention}"
            }
        } else {
            interaction.acknowledgeEphemeral().followUpEphemeral {
                embed {
                    log(Level.ERROR, "You don't have permissions to execute this command.")
                }
            }
        }
    }
}
