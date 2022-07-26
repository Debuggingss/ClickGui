package dev.debuggings.clickgui.elements

import dev.debuggings.clickgui.ClickGui
import dev.debuggings.clickgui.Colors
import dev.debuggings.clickgui.Section
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIText

open class Element<T>(var value: T) : UIBlock(Colors.OFF) {

    var clickGui: ClickGui? = null
    var section: Section? = null
    open var nameText: UIText? = null

    open fun loadValue() {}

    open fun saveValue() {}

    open fun init() {}

    infix fun elementOf(section: Section) = apply {
        section.addElement(this)
    }

    infix fun elementOf(subSection: SubSection) = apply {
        subSection.addElement(this)
    }
}
