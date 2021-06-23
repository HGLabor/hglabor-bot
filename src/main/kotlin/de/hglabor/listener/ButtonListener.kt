package de.hglabor.listener

import de.hglabor.*
import de.hglabor.database.MongoManager
import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.interaction.followUp
import dev.kord.core.behavior.interaction.followUpEphemeral
import dev.kord.core.entity.interaction.ComponentInteraction
import dev.kord.core.event.interaction.InteractionCreateEvent
import dev.kord.core.on
import kotlinx.coroutines.delay

@KordPreview
object ButtonListener {

    private val buttons = arrayListOf<RoleButton>()
    private val cooldowns = hashMapOf<Snowflake, Long>()

    fun registerButtons() {
        buttons.clear()
        for (button in MongoManager.roleButtons.find().toList()) {
            buttons.add(button)
        }
    }

    init {
        registerButtons()
        BotClient.client.on<InteractionCreateEvent> {
            if(interaction is ComponentInteraction) {
                if(cooldowns.containsKey(interaction.member().id)) {
                    if(cooldowns[interaction.member().id]!! > System.currentTimeMillis()) {
                        return@on
                    } else {
                        cooldowns.remove(interaction.member().id)
                    }
                }
                for (button in buttons) {
                    if(button.id == (interaction as ComponentInteraction).componentId) {
                        val roleId = Snowflake(button.roleId)
                        val response = interaction.acknowledgeEphemeral()
                        if(interaction.member().hasRole(roleId)) {
                            interaction.member().removeRole(roleId, "Role button")
                            response.followUpEphemeral {
                                content = "You got the role removed ${interaction.member().mention}"
                            }
                        } else {
                            interaction.member().addRole(roleId, "Role button")
                            response.followUpEphemeral {
                                content = "You got the role ${interaction.member().mention}"
                            }
                        }
                        cooldowns[interaction.member().id] = System.currentTimeMillis()+3000
                    }
                }
            }
        }
    }

}