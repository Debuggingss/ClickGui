package dev.debuggings.clickgui

import com.electronwill.nightconfig.core.file.FileConfig
import dev.debuggings.clickgui.handlers.ColorHandler
import dev.debuggings.clickgui.handlers.KeyBindHandler
import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.dsl.*
import net.minecraftforge.common.MinecraftForge
import java.awt.Color

class ClickGui(val config: FileConfig, val color: Color? = null) : WindowScreen(ElementaVersion.V1) {

    val sections: ArrayList<Section> = ArrayList()

    fun init() {
        config.load()
        sections.forEach {
            it.init()
        }

        MinecraftForge.EVENT_BUS.register(ColorHandler(this, color))
        MinecraftForge.EVENT_BUS.register(KeyBindHandler(this))
    }

    fun addSection(section: Section): Section {
        section.clickGui = this
        sections.add(section)
        window.addChild(section)

        window.children.forEachIndexed { index, comp ->
            val x = config.get<Double>("${section.name}.state.X") ?: (50 + (index * 30))
            val y = config.get<Double>("${section.name}.state.Y") ?: 50

            comp.setY(x.pixel())
            comp.setX(y.pixel())
        }

        return section
    }
}
