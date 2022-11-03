package dev.debuggings.clickgui.handlers

import dev.debuggings.clickgui.elements.*
import dev.debuggings.clickgui.ClickGui
import dev.debuggings.clickgui.Colors
import gg.essential.elementa.dsl.toConstraint
import gg.essential.universal.UMinecraft
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import java.awt.Color

class ColorHandler(private val clickGui: ClickGui, internal var color: Color? = null) {

    private val mc = UMinecraft.getMinecraft()
    private var colorRGB = 0F

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (event.phase != TickEvent.Phase.START) return
        if (mc.currentScreen != clickGui) return

        colorRGB += 0.01F

        clickGui.sections.forEach {
            it.elements.forEachIndexed { index, element ->
                handle(index, element)
            }
        }
    }

    private fun handle(index: Int, element: Element<*>) {
        if (element is DividerElement) return

        if (element is SubSection && element.elements.size > 0) {
            element.elements.forEachIndexed(::handle)
        }

        var clr = Color.getHSBColor(colorRGB + (index / 75F), 1F, 1F)

        if (color != null) clr = color

        when (element) {
            is ToggleElement, is SecureToggleElement -> if (element.value as Boolean) {
                element.nameText?.setColor(Color(255, 255, 255).toConstraint())
                element.setColor(clr.toConstraint())
                return
            }
            is SubSection -> if (element.value) {
                element.nameText?.setColor(Color(255, 255, 255).toConstraint())
                element.children[0].setColor(clr.toConstraint())
                return
            }
            is ButtonElement -> if (element.isHovered()) {
                element.nameText?.setColor(Color(255, 255, 255).toConstraint())
                element.setColor(clr.toConstraint())
                return
            }
        }

        element.nameText?.setColor(clr.toConstraint())

        if (element is SubSection) {
            element.children[0].setColor(Colors.OFF.toConstraint())
        } else {
            element.setColor(Colors.OFF.toConstraint())
        }
    }
}
