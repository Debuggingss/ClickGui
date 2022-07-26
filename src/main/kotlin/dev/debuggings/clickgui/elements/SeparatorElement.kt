package dev.debuggings.clickgui.elements

import dev.debuggings.clickgui.Colors
import gg.essential.elementa.components.UIText
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.pixel
import gg.essential.elementa.dsl.toConstraint
import gg.essential.elementa.effects.ScissorEffect

class SeparatorElement(name: String) : Element<String>("what the fuck did you expect?") {

    init {
        constrain {
            x = 0.pixel()
            y = 0.pixel()
            width = 100.pixel()
            height = 20.pixel()
            color = Colors.TITLE.toConstraint()

            enableEffect(ScissorEffect())
        }

        UIText(name).constrain {
            x = 4.pixel()
            y = 6.pixel()
            color = Colors.OPTION_TEXT.toConstraint()
        } childOf this
    }
}
