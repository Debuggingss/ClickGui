package dev.debuggings.clickgui.elements

import dev.debuggings.clickgui.ClickGui
import dev.debuggings.clickgui.Colors
import dev.debuggings.clickgui.Section
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIText

open class Element<T>(var elementName: String, var value: T) : UIBlock(Colors.OFF) {

    var clickGui: ClickGui? = null
    var section: Section? = null

    var savePath: String = ""

    open var nameText: UIText? = null

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
