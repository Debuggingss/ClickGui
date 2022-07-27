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

class ToggleElement(
    val name: String,
    val defaultValue: Boolean = false,
    private val saveState: Boolean = true,
    allowBinding: Boolean = false
) : KeyListener<Boolean>(name, defaultValue, allowBinding) {

    override fun loadValue() {
        value = clickGui!!.config.get<Boolean>(savePath) ?: defaultValue
        if (allowBinding) {
            boundKey = clickGui!!.config.get<Int>("keys.$savePath") ?: Keyboard.KEY_NONE
            boundKeyText.setText(Keyboard.getKeyName(boundKey))
        }
    }

    override fun saveValue() {
        if (!saveState) return
        clickGui!!.config.set<Boolean>(savePath, value)
        clickGui!!.config.save()
    }

    override var nameText: UIText? = UIText(name).constrain {
        x = 5.pixel()
        y = CenterConstraint()
        textScale = 0.5.pixel()
        color = Colors.OPTION_TEXT.toConstraint()
    } childOf this

    override fun init() {
        loadValue()

        if (!allowBinding) {
            boundKeyText.setText("")
        }

        onMouseClick { event ->
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && allowBinding) {
                listen(event)
            } else {
                value = !value
                saveValue()
            }
        }

        if (allowBinding) {
            captureKeyPress()
        }
    }
}
