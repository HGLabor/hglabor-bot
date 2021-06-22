package de.hglabor.config.data

data class DatabaseSettings(
    val host: String? = null,
    val port: Int? = null,
    val username: String? = null,
    val password: String? = null,
    val database: String? = null
)