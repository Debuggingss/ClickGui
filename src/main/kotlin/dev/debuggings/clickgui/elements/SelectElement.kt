package dev.debuggings.clickgui.elements

import dev.debuggings.clickgui.Colors
import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.pixel
import gg.essential.elementa.dsl.toConstraint
import gg.essential.elementa.effects.ScissorEffect

class SelectElement(
    private val name: String,
    private val defaultValue: String,
    private val options: ArrayList<String>
) : Element<String>(defaultValue) {

    private var selectedOption: Int = 0

    override fun loadValue() {
        value = clickGui!!.config.get<String>("${section?.name}.$name") ?: defaultValue
        selectedOption = options.indexOf(defaultValue)
        valueText.setText(value)
    }

    override fun saveValue() {
        clickGui!!.config.set<String>("${section?.name}.$name", value)
        clickGui!!.config.save()
    }

    override var nameText: UIText? = UIText(name).constrain {
        x = 5.pixel()
        y = CenterConstraint()
        textScale = 0.5.pixel()
        color = Colors.OPTION_TEXT.toConstraint()
    } childOf this

    private val valueText = UIText(value).constrain {
        x = 5.pixel(true)
        y = CenterConstraint()
        textScale = 0.5.pixel()
        color = Colors.ON.toConstraint()
    } childOf this

    private val leftIconText = UIText("◀").constrain {
        x = 3.pixel()
        y = CenterConstraint()
        color = Colors.OPTION_TEXT.toConstraint()
    } childOf this

    private val rightIconText = UIText("▶").constrain {
        x = 3.pixel(true)
        y = CenterConstraint()
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

        leftIconText.hide()
        rightIconText.hide()

        onMouseEnter {
            nameText?.hide()

            leftIconText.unhide()
            rightIconText.unhide()

            valueText.setX(CenterConstraint())
        }

        onMouseLeave {
            nameText?.unhide()

            leftIconText.hide()
            rightIconText.hide()

            valueText.setX(5.pixel(true))
        }

        leftIconText.onMouseClick {
            if (selectedOption > 0) selectedOption--
            value = options[selectedOption]
            valueText.setText(value)
            saveValue()
        }

        rightIconText.onMouseClick {
            if (selectedOption < options.size - 1) selectedOption++
            value = options[selectedOption]
            valueText.setText(value)
            saveValue()
        }
    }
}
