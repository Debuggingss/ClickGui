package dev.debuggings.clickgui

import com.electronwill.nightconfig.core.file.FileConfig
import dev.debuggings.clickgui.elements.*
import dev.debuggings.clickgui.handlers.ColorHandler
import dev.debuggings.clickgui.handlers.DescriptionHandler
import dev.debuggings.clickgui.handlers.KeyBindHandler
import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIWrappedText
import gg.essential.elementa.constraints.ChildBasedSizeConstraint
import gg.essential.elementa.dsl.*
import net.minecraftforge.common.MinecraftForge
import java.awt.Color

open class ClickGui @JvmOverloads constructor(configPath: String, color: Color? = null) : WindowScreen(ElementaVersion.V1) {
    private lateinit var colorHandler: ColorHandler
    var color = color
        set(value) {
            colorHandler.color = value
            field = value
        }

    val config: FileConfig = FileConfig.of(configPath)
    val sections = mutableListOf<Section>()

    val descBlock = UIBlock(Colors.TITLE).constrain {
        x = (-200).pixel
        y = 0.pixel
        width = 100.pixel
        height = ChildBasedSizeConstraint() + 8.pixel
    } childOf window

    val descText: UIWrappedText = UIWrappedText("").constrain {
        x = 4.pixel
        y = 4.pixel
        width = 92.pixel
        textScale = 0.5.pixel
    } childOf descBlock

    open fun init() {
        config.load()
        sections.forEach {
            it.init()
        }

        colorHandler = ColorHandler(this, color)
        MinecraftForge.EVENT_BUS.register(colorHandler)
        MinecraftForge.EVENT_BUS.register(KeyBindHandler(this))
        MinecraftForge.EVENT_BUS.register(DescriptionHandler(this))

        descText.setColor(Colors.TITLE_TEXT.toConstraint())
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

        fun button(
            name: String,
            allowBinding: Boolean = false,
            description: String? = null,
            action: (() -> Unit)
        ): ButtonElement {
            return addElement(ButtonElement(name, allowBinding, description, action))
        }

        fun colorPicker(
            name: String,
            defaultColor: Color,
            description: String? = null
        ): ColorPickerElement {
            return addElement(ColorPickerElement(name, defaultColor, description))
        }

        fun decimalSlider(
            name: String,
            minValue: Float,
            maxValue: Float,
            defaultValue: Float,
            description: String? = null
        ): DecimalSliderElement {
            return addElement(DecimalSliderElement(name, minValue, maxValue, defaultValue, description))
        }

        fun secureToggle(
            name: String,
            defaultValue: Boolean = false,
            description: String? = null
        ): SecureToggleElement {
            return addElement(SecureToggleElement(name, defaultValue, description))
        }

        fun selector(
            name: String,
            defaultValue: String,
            options: ArrayList<String>,
            description: String? = null
        ): SelectElement {
            return addElement(SelectElement(name, defaultValue, options, description))
        }

        fun divider(name: String): DividerElement {
            return addElement(DividerElement(name))
        }

        fun slider(
            name: String,
            minValue: Int,
            maxValue: Int,
            defaultValue: Int,
            description: String? = null
        ): SliderElement {
            return addElement(SliderElement(name, minValue, maxValue, defaultValue, description))
        }

        fun textInput(
            name: String,
            defaultValue: String,
            description: String? = null
        ): TextInputElement {
            return addElement(TextInputElement(name, defaultValue, description))
        }

        fun toggle(
            name: String,
            defaultValue: Boolean = false,
            saveState: Boolean = true,
            allowBinding: Boolean = false,
            description: String? = null
        ): ToggleElement {
            return addElement(ToggleElement(name, defaultValue, saveState, allowBinding, description))
        }
    }
}
