package de.hglabor.command.commands

import de.hglabor.command.SlashCommand
import dev.kord.common.annotation.KordPreview
import dev.kord.core.behavior.interaction.followUp
import dev.kord.core.entity.interaction.CommandInteraction

@KordPreview
object StatsCommand : SlashCommand(
    "stats",
    "Shows ingame stats on discord"
) {
    override suspend fun handleCommand(interaction: CommandInteraction) {
        interaction.acknowledgePublic().followUp {
            content = "waiting for royzer to make this possible..."
        }
    }
}