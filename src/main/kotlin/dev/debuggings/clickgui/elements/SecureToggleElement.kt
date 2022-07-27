package dev.debuggings.clickgui.elements

import dev.debuggings.clickgui.Colors
import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.pixel
import gg.essential.elementa.dsl.toConstraint
import gg.essential.elementa.effects.ScissorEffect

class SecureToggleElement(
    private val name: String,
    private val defaultValue: Boolean = false
) : Element<Boolean>(name, defaultValue) {

    private var clicks: Int = 0

    override fun loadValue() {
        value = clickGui!!.config.get<Boolean>(savePath) ?: defaultValue
    }

    override fun saveValue() {
        clickGui!!.config.set<Boolean>(savePath, value)
        clickGui!!.config.save()
    }

    private val iconText = UIText("!").constrain {
        x = 4.pixel()
        y = CenterConstraint()
        color = Colors.WARNING.toConstraint()
    } childOf this

    override var nameText: UIText? = UIText(name).constrain {
        x = 10.pixel()
        y = CenterConstraint()
        textScale = 0.5.pixel()
        color = Colors.OPTION_TEXT.toConstraint()
    } childOf this

    private var clickCountText = UIText("0/3").constrain {
        x = 5.pixel(true)
        y = CenterConstraint()
        textScale = 0.5.pixel()
        color = Colors.OPTION_TEXT.toConstraint()
    } childOf this

    override fun init() {
        loadValue()

        constrain {
            x = 0.pixel()
            y = 0.pixel()
            width = 100.pixel()
            height = 20.pixel()

            enableEffect(ScissorEffect())
        }

        onMouseClick {
            clicks++

            if (clicks == 3) {
                clicks = 0
                value = !value
                saveValue()
            }

            clickCountText.setText("$clicks/3")
        }

        onMouseLeave {
            clicks = 0
            clickCountText.setText("0/3")
        }
    }
}
