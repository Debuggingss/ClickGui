package dev.debuggings.clickgui.elements

import dev.debuggings.clickgui.Colors
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIText
import gg.essential.elementa.components.input.UITextInput
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.pixel
import gg.essential.elementa.dsl.toConstraint

class TextInputElement(
    name: String,
    private val defaultValue: String,
    override var description: String? = null,
) : Element<String>(name, defaultValue, description) {

    override fun loadValue() {
        value = clickGui!!.config.get<String>(savePath) ?: defaultValue
        textInput.setText(value)
    }

    override fun saveValue() {
        clickGui!!.config.set<String>(savePath, value)
        clickGui!!.config.save()
    }

    override var nameText: UIText? = UIText(name).constrain {
        x = 5.pixel()
        y = CenterConstraint()
        textScale = 0.5.pixel()
        color = Colors.OPTION_TEXT.toConstraint()
    } childOf this

    private val textInputBackground = UIBlock(Colors.BACKGROUND).constrain {
        x = 5.pixel()
        y = CenterConstraint()
        height = 14.pixel()
        width = 90.pixel()
    } childOf this

    private val textInput = (UITextInput().constrain {
        x = 2.pixel()
        y = CenterConstraint()
        height = 10.pixel()
        width = 86.pixel()
    }.onMouseClick {
        grabWindowFocus()
    } childOf textInputBackground) as UITextInput

    override fun init() {
        loadValue()

        textInput.onKeyType { _, _ ->
            value = textInput.getText()

            saveValue()
        }

        textInputBackground.hide()

        onMouseEnter {
            nameText?.hide()

            textInputBackground.unhide()
        }

        onMouseLeave {
            nameText?.unhide()

            textInputBackground.hide()
        }
    }
}
