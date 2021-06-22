package de.hglabor.listener

import de.hglabor.*
import de.hglabor.database.MongoManager
import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.Snowflake
import dev.kord.core.entity.interaction.ComponentInteraction
import dev.kord.core.event.interaction.InteractionCreateEvent
import dev.kord.core.on

@KordPreview
object ButtonListener {

    private val buttons = arrayListOf<RoleButton>()

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
                for (button in buttons) {
                    if(button.id == (interaction as ComponentInteraction).componentId) {
                        val roleId = Snowflake(button.roleId)
                        if(interaction.member().hasRole(roleId)) {
                            interaction.member().removeRole(roleId, "Role button")
                        } else {
                            interaction.member().addRole(roleId, "Role button")
                        }
                    }
                }
            }
        }
    }

}