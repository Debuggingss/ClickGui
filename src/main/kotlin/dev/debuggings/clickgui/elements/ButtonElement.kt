package dev.debuggings.clickgui.elements

import dev.debuggings.clickgui.Colors
import dev.debuggings.clickgui.listeners.KeyListener
import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.pixel
import gg.essential.elementa.dsl.toConstraint
import org.lwjgl.input.Keyboard

class ButtonElement(
    name: String,
    allowBinding: Boolean = false,
    val function: () -> Unit
) : KeyListener<String>(name, "null", allowBinding) {

    override fun loadValue() {
        if (allowBinding) {
            boundKey = clickGui!!.config.get<Int>("keys.$savePath") ?: Keyboard.KEY_NONE
            boundKeyText.setText(Keyboard.getKeyName(boundKey))
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
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && allowBinding) {
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
