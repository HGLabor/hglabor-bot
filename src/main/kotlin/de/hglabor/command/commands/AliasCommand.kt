package de.hglabor.command.commands

import de.hglabor.command.CommandManager
import de.hglabor.command.SlashCommand
import de.hglabor.data.Alias
import de.hglabor.database.MongoManager
import de.hglabor.guild
import de.hglabor.hasPermission
import de.hglabor.member
import dev.kord.common.Color
import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.Permission
import dev.kord.core.behavior.interaction.followUp
import dev.kord.core.entity.interaction.CommandInteraction
import dev.kord.core.entity.interaction.string
import dev.kord.rest.Image
import dev.kord.rest.builder.interaction.embed
import kotlinx.coroutines.delay
import org.litote.kmongo.bson

@KordPreview
object AliasCommand : SlashCommand(
    name = "alias",
    description = "Create an alias-tag to read.",
    {
        subCommand("create", "Create an alias-tag.") {
            string("key", "The key of the alias-tag.") {
                required = true
            }
            string("value", "The value of the alias-tag.") {
                required = true
            }
        }
        subCommand("delete", "Delete an alias-tag.") {
            string("alias", "The alias-tag to delete.") {
                required = true
                for (it in MongoManager.aliases.find().toList())
                    choice(it.key, it.key)
            }
        }
    }
) {
    override suspend fun handleCommand(interaction: CommandInteraction) {
        interaction.acknowledgePublic().followUp {
            if(interaction.member().hasPermission(Permission.ManageMessages)) {
                val key = interaction.command.options["key"]?.string()
                val value = interaction.command.options["value"]?.string()
                if (key != null && value != null) {
                    MongoManager.aliases.insertOne(Alias(key,value))
                    delay(1000)
                    CommandManager.reloadCommands()
                    embed {
                        color = Color(0,255,0)
                        title = "Alias created."
                        description = "You can read the alias-tag with `/tag post $key`"
                        footer {
                            icon = interaction.guild().getIconUrl(Image.Format.GIF)!!
                            text = interaction.guild().name
                        }
                    }
                } else {
                    val alias = interaction.command.options["alias"]?.string()
                    if(alias != null) {
                        MongoManager.aliases.deleteOne("{\"key\":\"${alias}\"}".bson)
                        delay(1000)
                        CommandManager.reloadCommands()
                        embed {
                            color = Color(255,0,0)
                            title = "Alias deleted."
                            description = "The alias $alias got deleted."
                            footer {
                                icon = interaction.guild().getIconUrl(Image.Format.GIF)!!
                                text = interaction.guild().name
                            }
                        }
                    }
                }
            }

        }
    }
}