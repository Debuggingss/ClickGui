package dev.debuggings.clickgui.elements

import dev.debuggings.clickgui.ClickGui
import dev.debuggings.clickgui.Colors
import dev.debuggings.clickgui.Section
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIText
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.effect
import gg.essential.elementa.dsl.pixel
import gg.essential.elementa.effects.ScissorEffect

open class Element<T>(
    var elementName: String, value: T,
    open var description: String? = null
) : UIBlock(Colors.OFF) {

    var value: T = value
        set(value) {
            listener(value)
            field = value
        }

    private var listener: (T) -> Unit = { }

    var clickGui: ClickGui? = null
    var section: Section? = null

    var savePath: String = ""

    open var nameText: UIText? = null

    init {
        constrain {
            x = 0.pixel()
            y = 0.pixel()
            width = 100.pixel()
            height = 20.pixel()
        } effect ScissorEffect()
    }

    /**
     * Runs callback whenever the value changes with its new value
     */
    fun onChange(callback: (T) -> Unit) {
        listener = callback
    }

    open fun loadValue() {}

    open fun saveValue() {}

    open fun init() {}

    infix fun elementOf(element: Any) = apply {
        when (element) {
            is Section -> element.addElement(this)
            is SubSection -> element.addElement(this)
            else -> throw IllegalArgumentException("element must be Section or SubSection")
        }
    }
}
