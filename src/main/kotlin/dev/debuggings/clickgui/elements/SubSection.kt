package dev.debuggings.clickgui.elements

import dev.debuggings.clickgui.Colors
import dev.debuggings.clickgui.Section
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.pixel
import gg.essential.elementa.dsl.toConstraint
import gg.essential.elementa.effects.ScissorEffect
import org.lwjgl.input.Keyboard

class SubSection(
    val name: String,
    private val defaultValue: Boolean = false,
    private val saveState: Boolean = true,
    private val allowBinding: Boolean = false,
    private val toggleFunctionality: Boolean = true
) : Element<Boolean>(defaultValue) {

    var subSection: SubSection? = null

    val elements: ArrayList<Element<*>> = ArrayList()

    var boundKey: Int = Keyboard.KEY_NONE
    var keyPressed: Boolean = false

    private var keyInputMode: Boolean = false

    override fun loadValue() {
        if (toggleFunctionality) {
            value = clickGui!!.config.get<Boolean>("${section?.name}.$name.value") ?: defaultValue
        }
        if (allowBinding) {
            boundKey = clickGui!!.config.get<Int>("keys.${section?.name}.$name") ?: Keyboard.KEY_NONE
            boundKeyText.setText(Keyboard.getKeyName(boundKey))
        }
    }

    override fun saveValue() {
        if (toggleFunctionality && saveState) {
            clickGui!!.config.set<Boolean>("${section?.name}.$name.value", value)
        }

        clickGui!!.config.save()
    }

    private fun saveKeybind() {
        if (allowBinding) {
            clickGui!!.config.set<Int>("keys.${section?.name}.$name", boundKey)
            clickGui!!.config.save()
        }
    }

    fun addElement(element: Element<*>): Element<*> {
        element.clickGui = section?.clickGui
        element.section = section
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

    private var boundKeyText = UIText("NONE").constrain {
        x = 5.pixel(true)
        y = CenterConstraint()
        textScale = 0.5.pixel()
        color = Colors.OPTION_TEXT.toConstraint()
    } childOf titleBar

    override fun init() {
        loadValue()

        constrain {
            x = CenterConstraint()
            y = CenterConstraint()
            width = 100.pixel()
            height = 20.pixel()
            color = Colors.BACKGROUND.toConstraint()

            enableEffect(ScissorEffect())
        }

        if (!allowBinding) {
            boundKeyText.setText("")
        }

        elements.forEach {
            it.init()
        }

        updatePositions()

        titleBar.onMouseClick { event ->
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && allowBinding && toggleFunctionality) {
                if (event.mouseButton == 0) {
                    keyInputMode = true
                    boundKeyText.setText("Waiting...")
                    return@onMouseClick
                }
                else if (event.mouseButton == 1) {
                    boundKey = Keyboard.KEY_NONE
                    boundKeyText.setText(Keyboard.getKeyName(boundKey))
                    saveKeybind()
                }
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
            clickGui?.window?.onKeyType { _, keyCode ->
                if (!keyInputMode) return@onKeyType
                if (keyCode == Keyboard.KEY_LSHIFT) return@onKeyType
                if (keyCode != Keyboard.KEY_ESCAPE) {
                    boundKey = keyCode
                    saveKeybind()
                }
                keyInputMode = false
                boundKeyText.setText(Keyboard.getKeyName(boundKey))
            }
        }
    }

    infix fun subSectionOf(section: Section) = apply {
        section.addElement(this)
    }

    infix fun subSectionOf(section: SubSection) = apply {
        this.subSection = section
        section.addElement(this)
    }
}
