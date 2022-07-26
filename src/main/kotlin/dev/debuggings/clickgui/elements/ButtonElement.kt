package dev.debuggings.clickgui.elements

import dev.debuggings.clickgui.Colors
import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.pixel
import gg.essential.elementa.dsl.toConstraint
import gg.essential.elementa.effects.ScissorEffect
import org.lwjgl.input.Keyboard

class ButtonElement(
    private val name: String,
    private val allowBinding: Boolean = false,
    val function: () -> Unit
) : Element<String>("null") {

    var boundKey: Int = Keyboard.KEY_NONE
    var keyPressed: Boolean = false

    private var keyInputMode: Boolean = false

    override fun loadValue() {
        if (allowBinding) {
            boundKey = clickGui!!.config.get<Int>("keys.${section?.name}.$name") ?: Keyboard.KEY_NONE
            boundKeyText.setText(Keyboard.getKeyName(boundKey))
        }
    }

    private fun saveKeybind() {
        if (allowBinding) {
            clickGui!!.config.set<Int>("keys.${section?.name}.$name", boundKey)
            clickGui!!.config.save()
        }
    }

    override var nameText: UIText? = UIText(name).constrain {
        x = 5.pixel()
        y = CenterConstraint()
        textScale = 0.5.pixel()
        color = Colors.OPTION_TEXT.toConstraint()
    } childOf this

    private var boundKeyText = UIText("NONE").constrain {
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

        if (!allowBinding) {
            boundKeyText.setText("")
        }

        onMouseClick { event ->
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && allowBinding) {
                if (event.mouseButton == 0) {
                    keyInputMode = true
                    boundKeyText.setText("Waiting...")
                    return@onMouseClick
                }
                else if (event.mouseButton == 1) {
                    boundKey = Keyboard.KEY_NONE
                    boundKeyText.setText(Keyboard.getKeyName(boundKey))
                    saveKeybind()
                }
            } else {
                function()
            }
        }

        if (allowBinding) {
            clickGui?.window?.onKeyType { _, keyCode ->
                if (!keyInputMode) return@onKeyType
                if (keyCode == Keyboard.KEY_LSHIFT) return@onKeyType
                if (keyCode != Keyboard.KEY_ESCAPE) {
                    boundKey = keyCode
                    saveKeybind()
                }
                keyInputMode = false
                boundKeyText.setText(Keyboard.getKeyName(boundKey))
            }
        }
    }
}
