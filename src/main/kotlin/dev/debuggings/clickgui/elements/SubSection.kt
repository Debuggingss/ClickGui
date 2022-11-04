package dev.debuggings.clickgui.elements

import dev.debuggings.clickgui.Colors
import dev.debuggings.clickgui.Section
import dev.debuggings.clickgui.Utils.configName
import dev.debuggings.clickgui.listeners.KeyListener
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.pixel
import gg.essential.elementa.dsl.toConstraint
import gg.essential.universal.UKeyboard

class SubSection(
    val name: String,
    private val defaultValue: Boolean = false,
    private val saveState: Boolean = true,
    allowBinding: Boolean = false,
    private val toggleFunctionality: Boolean = true,
    override var description: String? = null,
) : KeyListener<Boolean>(name, defaultValue, allowBinding, description) {

    var subSection: SubSection? = null

    val elements = mutableListOf<Element<*>>()

    override fun loadValue() {
        if (toggleFunctionality) {
            value = clickGui!!.config.get<Boolean>("$savePath._value_") ?: defaultValue
        }
        if (allowBinding) {
            boundKey = clickGui!!.config.get<Int>("_keys_.$savePath._key_") ?: UKeyboard.KEY_NONE
            boundKeyText.setText(UKeyboard.getKeyName(boundKey)!!)
        }
    }

    override fun saveValue() {
        if (toggleFunctionality && saveState) {
            clickGui!!.config.set<Boolean>("$savePath._value_", value)
        }

        clickGui!!.config.save()
    }

    fun addElement(element: Element<*>): Element<*> {
        element.clickGui = section?.clickGui
        element.section = section
        element.savePath = "$savePath.${element.elementName}".configName
        elements.add(element)
        this.addChild(element)

        return element
    }

    fun updateHeight(updateParent: Boolean = false) {
        if (getHeight() == 20.0F) {
            var height = 0F

            children.forEachIndexed { index, child ->
                if (index == 0) return@forEachIndexed
                height += child.getHeight() + 1
            }

            if (parent.children.indexOf(this) != parent.children.size - 1) height += 4

            setHeight((20 + height).pixel())
        } else {
            elements.forEach {
                if (it is SubSection) {
                    it.setHeight(100.pixel())
                    it.updateHeight()
                    it.saveValue()
                }
            }

            setHeight(20.pixel())
        }

        if (updateParent) {
            subSection?.setHeight(20F.pixel())
            subSection?.updateHeight(true)
            subSection?.updatePositions()

            section?.setHeight(28F.pixel())
            section?.updateHeight(false)
            section?.updatePositions()
        }
    }

    private fun updatePositions() {
        var height = 21F

        children.forEachIndexed { index, comp ->
            if (index == 0) return@forEachIndexed
            comp.setY(height.pixel())
            height += comp.getHeight() + 1
        }
    }

    private val titleBar = UIBlock(Colors.TITLE).constrain {
        x = 0.pixel()
        y = 0.pixel()
        width = 100.pixel()
        height = 20.pixel()
    } childOf this

    private val iconText = UIText("☰").constrain {
        x = 4.pixel()
        y = CenterConstraint()
        textScale = 0.5.pixel()
        color = Colors.TITLE_TEXT.toConstraint()
    } childOf titleBar

    override var nameText: UIText? = UIText(name).constrain {
        x = 10.pixel()
        y = CenterConstraint()
        textScale = 0.5.pixel()
        color = Colors.TITLE_TEXT.toConstraint()
    } childOf titleBar


    override fun init() {
        loadValue()

        constrain {
            color = Colors.BACKGROUND.toConstraint()
        }

        boundKeyText childOf titleBar

        if (!allowBinding) {
            boundKeyText.setText("")
        }

        elements.forEach {
            it.init()
        }

        updatePositions()

        titleBar.onMouseClick { event ->
            if (UKeyboard.isKeyDown(UKeyboard.KEY_LSHIFT) && allowBinding && toggleFunctionality) {
                listen(event)
            } else {
                if (toggleFunctionality && event.mouseButton == 0) {
                    value = !value
                    saveValue()
                }
                else if (event.mouseButton == 1) {
                    updateHeight(true)
                }
            }
        }

        if (toggleFunctionality && allowBinding) {
            captureKeyPress()
        }
    }

    infix fun subSectionOf(section: Any) = apply {
        when (section) {
            is Section -> section.addElement(this)
            is SubSection -> {
                this.subSection = section
                section.addElement(this)
            }
            else -> throw IllegalArgumentException("section must be Section or SubSection")
        }
    }
}
