package dev.debuggings.clickgui.elements

import dev.debuggings.clickgui.Colors
import gg.essential.elementa.components.UIText
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.pixel
import gg.essential.elementa.dsl.toConstraint

class DividerElement @JvmOverloads constructor(name: String? = null) : Element<String>("", "") {

    fun setHeight(height: Number) = constrain {
        this.height = height.pixel()
    }

    init {
        constrain {
            color = Colors.TITLE.toConstraint()
        }

        name?.let {
            UIText(it).constrain {
                x = 4.pixel()
                y = 6.pixel()
                color = Colors.OPTION_TEXT.toConstraint()
            } childOf this
        }
    }
}
