package dev.debuggings.clickgui.handlers

import dev.debuggings.clickgui.ClickGui
import dev.debuggings.clickgui.elements.*
import gg.essential.elementa.dsl.pixel
import gg.essential.universal.UMinecraft
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent

class DescriptionHandler(private val clickGui: ClickGui) {

    private val mc = UMinecraft.getMinecraft()
    private var descriptionShown = false

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (event.phase != TickEvent.Phase.START) return
        if (mc.currentScreen != clickGui) return

        descriptionShown = false

        clickGui.sections.forEach {
            it.elements.forEach { element ->
                handle(element)
            }
        }

        if (!descriptionShown) {
            clickGui.descBlock.setX((-200).pixel)
        }
    }

    private fun handle(element: Element<*>) {
        if (element is DividerElement) return

        if (element is SubSection && element.elements.size > 0) {
            element.elements.forEach(::handle)
        }

        if (descriptionShown) return
        if (!element.isHovered()) return
        if (element is SubSection && !element.children[0].isHovered()) return
        if (element.parent.getHeight() == 20F || element.section?.getHeight() == 28F) return

        val desc = element.description ?: return
        descriptionShown = true

        clickGui.descText.setText(desc)

        val x = if (element.getRight() + 105 > clickGui.window.getWidth()) {
            (element.getLeft() - 105).pixel
        } else {
            (element.getRight() + 5).pixel
        }

        clickGui.descBlock.setX(x)
        clickGui.descBlock.setY(element.getTop().pixel)
        clickGui.descBlock.setFloating(true)
    }
}
