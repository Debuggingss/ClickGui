package dev.debuggings.clickgui

import com.electronwill.nightconfig.core.file.FileConfig
import dev.debuggings.clickgui.elements.*
import dev.debuggings.clickgui.handlers.ColorHandler
import dev.debuggings.clickgui.handlers.KeyBindHandler
import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.dsl.*
import net.minecraftforge.common.MinecraftForge
import java.awt.Color

open class ClickGui(configPath: String, val color: Color? = null) : WindowScreen(ElementaVersion.V1) {

    val config: FileConfig = FileConfig.of(configPath)
    val sections = mutableListOf<Section>()

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
            val x = config.get<Double>("${section.name}._state_.X") ?: (50 + (index * 30))
            val y = config.get<Double>("${section.name}._state_.Y") ?: 50

            comp.setY(x.pixel())
            comp.setX(y.pixel())
        }

        return section
    }

    // --- DSL API ---

    fun section(name: String, builder: SectionBuilder<Section>.() -> Unit) {
        val sectionBuilder = SectionBuilder<Section>()

        sectionBuilder.section = Section(name)

        sectionBuilder.section!! sectionOf this
        sectionBuilder.apply(builder)
    }

    class SectionBuilder<T> {
        var section: T? = null
            internal set

        private fun <T : Element<*>> addElement(element: T): T {
            element elementOf section!!
            return element
        }

        fun subsection(subsectionName: String, builder: SectionBuilder<SubSection>.() -> Unit) {
            val subsection = SectionBuilder<SubSection>()
            subsection.section = SubSection(subsectionName)
            subsection.section!! subSectionOf section!!
            subsection.apply(builder)
        }

        fun button(name: String, allowBinding: Boolean = false, action: (() -> Unit)): ButtonElement {
            return addElement(ButtonElement(name, allowBinding, action))
        }

        fun colorPicker(name: String, defaultColor: Color): ColorPickerElement {
            return addElement(ColorPickerElement(name, defaultColor))
        }

        fun decimalSlider(name: String, minValue: Float, maxValue: Float, defaultValue: Float): DecimalSliderElement {
            return addElement(DecimalSliderElement(name, minValue, maxValue, defaultValue))
        }

        fun secureToggle(name: String, defaultValue: Boolean = false): SecureToggleElement {
            return addElement(SecureToggleElement(name, defaultValue))
        }

        fun selector(name: String, defaultValue: String, options: ArrayList<String>): SelectElement {
            return addElement(SelectElement(name, defaultValue, options))
        }

        fun divider(name: String): DividerElement {
            return addElement(DividerElement(name))
        }

        fun slider(name: String, minValue: Int, maxValue: Int, defaultValue: Int): SliderElement {
            return addElement(SliderElement(name, minValue, maxValue, defaultValue))
        }

        fun textInput(name: String, defaultValue: String): TextInputElement {
            return addElement(TextInputElement(name, defaultValue))
        }

        fun toggle(name: String, defaultValue: Boolean = false, saveState: Boolean = true, allowBinding: Boolean = false): ToggleElement {
            return addElement(ToggleElement(name, defaultValue, saveState, allowBinding))
        }
    }
}
