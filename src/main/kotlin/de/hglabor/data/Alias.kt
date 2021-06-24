package de.hglabor.data

import kotlinx.serialization.Serializable

@Serializable
data class Alias(val key: String, val value: String)
