package dev.debuggings.clickgui.handlers

import dev.debuggings.clickgui.ClickGui
import dev.debuggings.clickgui.elements.ButtonElement
import dev.debuggings.clickgui.elements.Element
import dev.debuggings.clickgui.elements.ToggleElement
import dev.debuggings.clickgui.elements.SubSection
import gg.essential.universal.UKeyboard
import gg.essential.universal.UMinecraft
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent

class KeyBindHandler(private val clickGui: ClickGui) {

    private val mc = UMinecraft.getMinecraft()

    @SubscribeEvent
    fun onKeyInput(event: InputEvent.KeyInputEvent) {
        if (mc.thePlayer == null) return

        clickGui.sections.forEach { section ->
            section.elements.forEach { element ->
                handle(element)
            }
        }
    }

    private fun handle(element: Element<*>) {
        if (element is SubSection && element.elements.size > 0) {
            element.elements.forEach(::handle)
        }

        when (element) {
            is ToggleElement -> {
                if (element.boundKey == UKeyboard.KEY_NONE) return

                if (UKeyboard.isKeyDown(element.boundKey) && !element.keyPressed) {
                    element.value = !element.value
                    element.saveValue()

                    element.keyPressed = true
                } else if (!UKeyboard.isKeyDown(element.boundKey) && element.keyPressed) {
                    element.keyPressed = false
                }
            }
            is SubSection -> {
                if (element.boundKey == UKeyboard.KEY_NONE) return

                if (UKeyboard.isKeyDown(element.boundKey) && !element.keyPressed) {
                    element.value = !element.value
                    element.saveValue()

                    element.keyPressed = true
                } else if (!UKeyboard.isKeyDown(element.boundKey) && element.keyPressed) {
                    element.keyPressed = false
                }
            }
            is ButtonElement -> {
                if (element.boundKey == UKeyboard.KEY_NONE) return

                if (UKeyboard.isKeyDown(element.boundKey) && !element.keyPressed) {
                    element.function()

                    element.keyPressed = true
                } else if (!UKeyboard.isKeyDown(element.boundKey) && element.keyPressed) {
                    element.keyPressed = false
                }
            }
        }
    }
}
