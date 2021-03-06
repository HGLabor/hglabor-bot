package de.hglabor.http

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.Reader
import java.net.URL
import java.nio.charset.Charset

object HttpUtils {

    private val GSON = GsonBuilder().setPrettyPrinting().create()

    private fun readAll(rd: Reader): String {
        val sb = StringBuilder()
        var cp: Int
        while (rd.read().also { cp = it } != -1) {
            sb.append(cp.toChar())
        }
        return sb.toString()
    }

    fun readJsonFromUrl(url: String?): JsonObject? {
        val `is`: InputStream = URL(url).openStream()
        return `is`.use { dies ->
            val rd = BufferedReader(InputStreamReader(dies, Charset.forName("UTF-8")))
            val jsonText = readAll(rd)
            JsonParser().parse(jsonText).getAsJsonObject()
        }
    }

    fun toJson(obj: Any?): JsonObject? {
        return JsonParser().parse(GSON.toJson(obj)).asJsonObject
    }

    fun fromJsonString(json: String?): JsonObject? {
        return JsonParser().parse(json).asJsonObject
    }

    fun <T> fromJson(json: JsonObject?, type: Class<T>?): T {
        return GSON.fromJson(json, type)
    }

    fun getOpenGitHubIssues(user: String, repository: String): Int {
        return readJsonFromUrl("https://api.github.com/search/issues?q=repo:${user}/${repository}+type:issue+state:open")?.getAsJsonPrimitive("total_count")?.asInt ?: 0
    }

    fun getPlayerCount(serverIp: String): String {
        return readJsonFromUrl("https://api.mcsrvstat.us/2/$serverIp")?.get("players")?.asJsonObject?.get("online").toString()
    }

    fun getMotdHeader(serverIp: String): String {
        return readJsonFromUrl("https://api.mcsrvstat.us/2/$serverIp")?.getAsJsonObject("motd")?.getAsJsonArray("clean")?.toString() ?: serverIp
    }

}