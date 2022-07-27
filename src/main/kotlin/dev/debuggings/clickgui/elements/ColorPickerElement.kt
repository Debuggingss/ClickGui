package dev.debuggings.clickgui.elements

import dev.debuggings.clickgui.Colors
import dev.debuggings.clickgui.Utils
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIRoundedRectangle
import gg.essential.elementa.components.UIText
import gg.essential.elementa.components.input.UITextInput
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.pixel
import gg.essential.elementa.dsl.toConstraint
import gg.essential.elementa.effects.ScissorEffect
import java.awt.Color

class ColorPickerElement(
    private val name: String,
    private val defaultValue: Color
) : Element<Color>(name, defaultValue) {

    override fun loadValue() {
        value = Utils.hexToColor(
            clickGui!!.config.get(savePath) ?: Utils.colorToHex(defaultValue)
        )
        textInput.setText(Utils.colorToHex(value))
        colorRectangle.setColor(value)
    }

    override fun saveValue() {
        clickGui!!.config.set<String>(savePath, Utils.colorToHex(value))
        clickGui!!.config.save()
    }

    override var nameText: UIText? = UIText(name).constrain {
        x = 5.pixel()
        y = CenterConstraint()
        textScale = 0.5.pixel()
        color = Colors.OPTION_TEXT.toConstraint()
    } childOf this

    private val colorRectangle = UIRoundedRectangle(4F).constrain {
        x = 5.pixel(true)
        y = CenterConstraint()
        width = 12.pixel()
        height = 12.pixel()
        color = defaultValue.toConstraint()
    } childOf this

    private val hashtagIcon = UIText("#").constrain {
        x = 3.pixel()
        y = CenterConstraint()
        color = Colors.OPTION_TEXT.toConstraint()
    } childOf this

    private val textInputBackground = UIBlock(Colors.BACKGROUND).constrain {
        x = 10.pixel()
        y = CenterConstraint()
        height = 14.pixel()
        width = 52.pixel()
    } childOf this

    private val textInput = UITextInput().constrain {
        x = 2.pixel()
        y = CenterConstraint()
        height = 10.pixel()
        width = 48.pixel()
    } childOf textInputBackground

    override fun init() {
        loadValue()

        textInput.onMouseClick {
            grabWindowFocus()
        }

        textInput.onKeyType { _, _ ->
            val filtered = textInput.getText().filter { c -> "abcdef0123456789".contains(c, true)}

            textInput.setText(filtered.uppercase().take(8))

            value = Utils.hexToColor(textInput.getText())
            colorRectangle.setColor(value)

            saveValue()
        }

        hashtagIcon.hide()
        textInputBackground.hide()

        onMouseEnter {
            nameText?.hide()

            hashtagIcon.unhide()
            textInputBackground.unhide()
        }

        onMouseLeave {
            nameText?.unhide()

            hashtagIcon.hide()
            textInputBackground.hide()
        }
    }
}
