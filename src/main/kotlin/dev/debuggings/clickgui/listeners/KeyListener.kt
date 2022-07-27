package dev.debuggings.clickgui.listeners

import dev.debuggings.clickgui.Colors
import dev.debuggings.clickgui.elements.Element
import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.pixel
import gg.essential.elementa.dsl.toConstraint
import gg.essential.elementa.events.UIClickEvent
import gg.essential.universal.UKeyboard

open class KeyListener<T>(
    name: String,
    defaultValue: T,
    val allowBinding: Boolean,
) : Element<T>(name, defaultValue) {

    private var keyInputMode: Boolean = false

    var boundKey: Int = UKeyboard.KEY_NONE
    var keyPressed: Boolean = false

    val boundKeyText: UIText = UIText("NONE").constrain {
        x = 5.pixel(true)
        y = CenterConstraint()
        textScale = 0.5.pixel()
        color = Colors.OPTION_TEXT.toConstraint()
    }

    fun listen(event: UIClickEvent) {
        if (event.mouseButton == 0) {
            keyInputMode = true
            boundKeyText.setText("Waiting...")
            return
        }
        else if (event.mouseButton == 1) {
            boundKey = UKeyboard.KEY_NONE
            boundKeyText.setText(UKeyboard.getKeyName(boundKey)!!)
            saveKeybind()
        }
    }

    fun saveKeybind() {
        if (allowBinding) {
            clickGui!!.config.set<Int>("keys.$savePath.key", boundKey)
            clickGui!!.config.save()
        }
    }

    fun captureKeyPress() {
        clickGui?.window?.onKeyType { _, keyCode ->
            if (!keyInputMode) return@onKeyType
            if (keyCode == UKeyboard.KEY_LSHIFT) return@onKeyType
            if (keyCode != UKeyboard.KEY_ESCAPE) {
                boundKey = keyCode
                saveKeybind()
            }
            keyInputMode = false
            boundKeyText.setText(UKeyboard.getKeyName(boundKey)!!)
        }
    }
}
