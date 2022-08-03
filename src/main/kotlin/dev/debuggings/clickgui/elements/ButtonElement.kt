package dev.debuggings.clickgui.elements

import dev.debuggings.clickgui.Colors
import dev.debuggings.clickgui.listeners.KeyListener
import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.pixel
import gg.essential.elementa.dsl.toConstraint
import gg.essential.universal.UKeyboard

class ButtonElement(
    name: String,
    allowBinding: Boolean = false,
    val function: () -> Unit
) : KeyListener<String>(name, "null", allowBinding) {

    override fun loadValue() {
        if (allowBinding) {
            boundKey = clickGui!!.config.get<Int>("_keys_.$savePath") ?: UKeyboard.KEY_NONE
            boundKeyText.setText(UKeyboard.getKeyName(boundKey)!!)
        }
    }

    override var nameText: UIText? = UIText(name).constrain {
        x = 5.pixel()
        y = CenterConstraint()
        textScale = 0.5.pixel()
        color = Colors.OPTION_TEXT.toConstraint()
    } childOf this

    override fun init() {
        loadValue()

        boundKeyText childOf this

        if (!allowBinding) {
            boundKeyText.setText("")
        }

        onMouseClick { event ->
            if (UKeyboard.isKeyDown(UKeyboard.KEY_LSHIFT) && allowBinding) {
                listen(event)
            } else {
                function()
            }
        }

        if (allowBinding) {
            captureKeyPress()
        }
    }
}
