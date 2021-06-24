package de.hglabor.timers

import de.hglabor.BotClient
import de.hglabor.http.HttpUtils
import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.ActivityType
import dev.kord.gateway.builder.PresenceBuilder
import kotlinx.coroutines.*
import java.util.*

fun launchTimer() {
    val timer = Timer()
    timer.schedule(PresenceTimer(), 10000, 30000)
}

class PresenceTimer : TimerTask() {

    private val statusScope = CoroutineScope(Dispatchers.IO)

    @KordPreview
    override fun run() {
        val activity = Activity.all.random()
        BotClient.logger.debug("Changing activity to ${activity.what} (${activity.activityType.name.lowercase()})")
        statusScope.launch {
            BotClient.client.editPresence {
                if(activity.activityType == ActivityType.Streaming) {
                    activity.specialData?.let {
                        streaming(activity.what, it)
                    }
                } else {
                    when(activity.activityType) {
                        ActivityType.Game -> playing(activity.what()) //gaming() would be cooler 😎
                        ActivityType.Listening -> listening(activity.what())
                        ActivityType.Competing -> competing(activity.what())
                        ActivityType.Watching -> watching(activity.what())
                        ActivityType.Unknown -> BotClient.logger.fatal("Activity type cannot be \"Unknown\", sorry Unknownjesus")
                        ActivityType.Custom -> BotClient.logger.fatal("Activity type cannot be \"Custom\"")
                        else -> BotClient.logger.fatal("Activity type cannot be null", NullPointerException("Activity type is null"))
                    }
                }
            }
        }
    }
}

@KordPreview
fun PresenceBuilder.applyActivity(activity: Activity) {
    if(activity.activityType == ActivityType.Streaming) {
        activity.specialData?.let {
            streaming(activity.what(), it)
        }
    } else {
        when(activity.activityType) {
            ActivityType.Game -> playing(activity.what()) //gaming() would be cooler 😎
            ActivityType.Listening -> listening(activity.what())
            ActivityType.Competing -> competing(activity.what())
            ActivityType.Watching -> watching(activity.what())
            ActivityType.Unknown -> BotClient.logger.fatal("Activity type cannot be \"Unknown\", sorry Unknownjesus")
            ActivityType.Custom -> BotClient.logger.fatal("Activity type cannot be \"Custom\"")
            else -> BotClient.logger.fatal("Activity type cannot be null", NullPointerException("Activity type is null"))
        }
    }
}

data class Activity(val activityType: ActivityType, val what: String, val specialData: String? = null) {

    @KordPreview
    fun what(): String {
        return what
            .replace("\${issues}", HttpUtils.getOpenGitHubIssues("hglabor", "hglabor-bot").toString())
            .replace("\${players}", HttpUtils.getPlayerCount("hglabor.de"))
    }

    companion object {
        val HG_EVENT = Activity(ActivityType.Competing, "einem HGEvent")
        val HGLABOR = Activity(ActivityType.Game, "auf HGLabor.de")
        val HELP = Activity(ActivityType.Listening, "/help")
        val ISSUES = Activity(ActivityType.Watching, "\${issues} issues")
        val GITHUB = Activity(ActivityType.Game, "github.com/HGLabor")
        val SMOOTHYY = Activity(ActivityType.Streaming, "hg auf hglabor", "https://www.twitch.tv/smoothyy___")
        val PLAYERS = Activity(ActivityType.Listening, "\${players} spielern")

        val all = listOf(
            HG_EVENT,
            HGLABOR,
            SMOOTHYY,
            HELP,
            ISSUES,
            GITHUB,
            PLAYERS
        )
    }
}