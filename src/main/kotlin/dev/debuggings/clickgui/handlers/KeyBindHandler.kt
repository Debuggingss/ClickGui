package dev.debuggings.clickgui.handlers

import dev.debuggings.clickgui.ClickGui
import dev.debuggings.clickgui.elements.ButtonElement
import dev.debuggings.clickgui.elements.Element
import dev.debuggings.clickgui.elements.ToggleElement
import dev.debuggings.clickgui.elements.SubSection
import net.minecraft.client.Minecraft
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent
import org.lwjgl.input.Keyboard

class KeyBindHandler(private val clickGui: ClickGui) {

    private val mc = Minecraft.getMinecraft()

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
                if (element.boundKey == Keyboard.KEY_NONE) return

                if (Keyboard.isKeyDown(element.boundKey) && !element.keyPressed) {
                    element.value = !element.value
                    element.saveValue()

                    element.keyPressed = true
                } else if (!Keyboard.isKeyDown(element.boundKey) && element.keyPressed) {
                    element.keyPressed = false
                }
            }
            is SubSection -> {
                if (element.boundKey == Keyboard.KEY_NONE) return

                if (Keyboard.isKeyDown(element.boundKey) && !element.keyPressed) {
                    element.value = !element.value
                    element.saveValue()

                    element.keyPressed = true
                } else if (!Keyboard.isKeyDown(element.boundKey) && element.keyPressed) {
                    element.keyPressed = false
                }
            }
            is ButtonElement -> {
                if (element.boundKey == Keyboard.KEY_NONE) return

                if (Keyboard.isKeyDown(element.boundKey) && !element.keyPressed) {
                    element.function()

                    element.keyPressed = true
                } else if (!Keyboard.isKeyDown(element.boundKey) && element.keyPressed) {
                    element.keyPressed = false
                }
            }
        }
    }
}
