package de.hglabor.command.commands

import de.hglabor.command.SlashCommand
import de.hglabor.database.MongoManager
import dev.kord.common.annotation.KordPreview
import dev.kord.core.behavior.interaction.followUp
import dev.kord.core.entity.interaction.CommandInteraction
import dev.kord.core.entity.interaction.string
import org.litote.kmongo.findOne

@KordPreview
object PostCommand : SlashCommand(
    name = "post",
    description = "Posts an alias.",
    {
        subCommand("post", "Posts an alias-tag.") {
            string("entry", "Select an valid alias-tag.") {
                required = true
                for (it in MongoManager.aliases.find())
                    choice(it.key, it.key)
            }
        }
        subCommand("list", "Shows all avaible aliases.")
    }
) {
    override suspend fun handleCommand(interaction: CommandInteraction) {
        interaction.acknowledgePublic().followUp {
            val entry = interaction.command.options["entry"]?.string()
            content = if (entry != null) {
                val alias = MongoManager.aliases.findOne("{\"key\":\"${entry}\"}")
                alias?.value ?: "This alias could not been found. Try `/tag list`"
            } else {
                MongoManager.aliases.find().toList().joinToString()
            }
        }

    }
}