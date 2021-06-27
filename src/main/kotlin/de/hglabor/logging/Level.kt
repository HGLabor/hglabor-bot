package de.hglabor.logging

enum class Level(val decimalColor: Int, var isEnabled: Boolean = true) {

    DEBUG(4244118, isEnabled = false),
    INFO(16777215),
    WARN(14395978),
    ERROR(15157354),
    FATAL(16711680)

}