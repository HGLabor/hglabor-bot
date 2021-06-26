package de.hglabor.command.commands

import de.hglabor.command.SlashCommand
import dev.kord.common.annotation.KordPreview
import dev.kord.core.behavior.interaction.followUp
import dev.kord.core.entity.interaction.CommandInteraction

@KordPreview
object RandomCommand : SlashCommand(
    "random",
    "Random survival quotes"
) {

    val quotes = listOf(
        "https://cdn.discordapp.com/attachments/832259408058777600/850000318926422046/unknown.png",
        "https://cdn.discordapp.com/attachments/766732517583355936/836670389655568394/unknown.png",
        "https://cdn.discordapp.com/attachments/766732517583355936/836678373127880764/unknown.png",
        "https://cdn.discordapp.com/attachments/766732517583355936/836678988042076208/unknown.png",
        "hast du eig ein bisschen obsi für mich xD",
        "https://cdn.discordapp.com/attachments/832259408058777600/836674250097819698/unknown.png",
        "https://cdn.discordapp.com/attachments/832259408058777600/836675077167775844/unknown.png",
        "https://cdn.discordapp.com/attachments/832259408058777600/836676060351168522/unknown.png",
        "https://cdn.discordapp.com/attachments/832259408058777600/836681005444104292/unknown.png",
        "https://cdn.discordapp.com/attachments/766732517583355936/849019409670602824/unknown.png",
        "https://cdn.discordapp.com/attachments/766732517583355936/849015046902120448/unknown.png\n" +
        "https://cdn.discordapp.com/attachments/766732517583355936/849015109606309909/unknown.png",
        "https://cdn.discordapp.com/attachments/766732517583355936/837814904790319124/Screenshot_2021-05-01-00-16-47-020.jpeg",
        "scheiß server wegen heckern",
        "https://i.imgur.com/CCBxmWQ.png",
        "https://i.imgur.com/pyhxKAV.png",
        "https://i.imgur.com/Hd7nYky.png",
        "https://cdn.discordapp.com/attachments/777539157903671316/858285158183206912/unknown.png"
    )

    override suspend fun handleCommand(interaction: CommandInteraction) {
        interaction.acknowledgePublic().followUp {
            content = quotes.random()
        }
    }
}