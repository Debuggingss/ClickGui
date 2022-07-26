package dev.debuggings.clickgui

import java.awt.Color

object Utils {

    fun map(number: Int, in_min: Int, in_max: Int, out_min: Int, out_max: Int): Int {
        return (number - in_min) * (out_max - out_min) / (in_max - in_min) + out_min
    }

    fun mapFloat(number: Float, in_min: Float, in_max: Float, out_min: Float, out_max: Float): Float {
        return (number - in_min) * (out_max - out_min) / (in_max - in_min) + out_min
    }

    fun colorToHex(color: Color): String {
        return (color.red.toString(16) +
                color.green.toString(16) +
                color.blue.toString(16) +
                color.alpha.toString(16)).uppercase()
    }

    fun hexToColor(hexColor: String): Color {
        val hex = hexColor.replace("#", "")

        return when (hex.length) {
            3 -> Color(
                Integer.valueOf(hex.substring(0, 1).repeat(2), 16),
                Integer.valueOf(hex.substring(1, 2).repeat(2), 16),
                Integer.valueOf(hex.substring(2, 3).repeat(2), 16)
            )
            4 -> Color(
                Integer.valueOf(hex.substring(0, 1).repeat(2), 16),
                Integer.valueOf(hex.substring(1, 2).repeat(2), 16),
                Integer.valueOf(hex.substring(2, 3).repeat(2), 16),
                Integer.valueOf(hex.substring(3, 4).repeat(2), 16)
            )
            6 -> Color(
                Integer.valueOf(hex.substring(0, 2), 16),
                Integer.valueOf(hex.substring(2, 4), 16),
                Integer.valueOf(hex.substring(4, 6), 16)
            )
            8 -> Color(
                Integer.valueOf(hex.substring(0, 2), 16),
                Integer.valueOf(hex.substring(2, 4), 16),
                Integer.valueOf(hex.substring(4, 6), 16),
                Integer.valueOf(hex.substring(6, 8), 16)
            )
            else -> Color(0, 0, 0)
        }
    }
}
