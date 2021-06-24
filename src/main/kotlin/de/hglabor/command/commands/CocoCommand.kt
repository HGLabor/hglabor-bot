package de.hglabor.command.commands

import de.hglabor.command.SlashCommand
import dev.kord.common.annotation.KordPreview
import dev.kord.core.behavior.interaction.followUp
import dev.kord.core.entity.interaction.CommandInteraction

@KordPreview
object CocoCommand  : SlashCommand(
    name = "coco",
    description = "Post a random picture of coco"
) {

    val list = listOf(
        "https://cdn.discordapp.com/attachments/796763365115559996/820008212740046896/unknown.png",
        "https://cdn.discordapp.com/attachments/796763365115559996/820008246349267015/unknown.png",
        "https://cdn.discordapp.com/attachments/796763365115559996/820008285452501052/unknown.png",
        "https://cdn.discordapp.com/attachments/796763365115559996/820008309830057984/unknown.png",
        "https://cdn.discordapp.com/attachments/796763365115559996/820008337491492965/unknown.png",
        "https://cdn.discordapp.com/attachments/796763365115559996/820008403417956372/unknown.png",
        "https://cdn.discordapp.com/attachments/796763365115559996/820008439194452008/unknown.png",
        "https://cdn.discordapp.com/attachments/796763365115559996/820008494950121482/unknown.png",
        "https://cdn.discordapp.com/attachments/796763365115559996/820008540445999124/unknown.png",
        "https://cdn.discordapp.com/attachments/796763365115559996/820008610142355476/unknown.png",
        "https://cdn.discordapp.com/attachments/796763365115559996/820008632648466442/unknown.png",
        "https://cdn.discordapp.com/attachments/796763365115559996/820008736692633651/unknown.png",
        "https://cdn.discordapp.com/attachments/796763365115559996/820008772898127872/unknown.png",
        "https://cdn.discordapp.com/attachments/796763365115559996/820008799191957524/unknown.png",
        "https://cdn.discordapp.com/attachments/796763365115559996/820008898848620554/unknown.png",
        "https://cdn.discordapp.com/attachments/796763365115559996/820008929395867718/unknown.png",
        "https://cdn.discordapp.com/attachments/796763365115559996/820008962572156988/unknown.png",
        "https://cdn.discordapp.com/attachments/796763365115559996/820008992909950976/unknown.png",
        "https://cdn.discordapp.com/attachments/796763365115559996/820009021364371476/unknown.png",
        "https://cdn.discordapp.com/attachments/796763365115559996/820009127128334336/unknown.png",
        "https://cdn.discordapp.com/attachments/796763365115559996/820009177070043144/unknown.png",
        "https://cdn.discordapp.com/attachments/796763365115559996/820009206391636079/unknown.png",
        "https://cdn.discordapp.com/attachments/796763365115559996/820009352723300442/unknown.png",
        "https://cdn.discordapp.com/attachments/796763365115559996/820009381638569994/unknown.png",
        ":flushed:",
        "https://cdn.discordapp.com/attachments/796763365115559996/820009420872351754/unknown.png",
        "https://cdn.discordapp.com/attachments/796763365115559996/820009593689735229/unknown.png",
        "https://cdn.discordapp.com/attachments/796763365115559996/820009665840676864/unknown.png",
        "https://cdn.discordapp.com/attachments/796763365115559996/820009690959446036/unknown.png",
        "https://cdn.discordapp.com/attachments/796763365115559996/820009807007842324/unknown.png",
        "https://cdn.discordapp.com/attachments/796763365115559996/820010289860050974/unknown.png"
    )

    override suspend fun handleCommand(interaction: CommandInteraction) {
        interaction.acknowledgePublic().followUp {
            content = list.random()
        }
    }
}