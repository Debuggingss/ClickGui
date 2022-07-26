package dev.debuggings.clickgui

import dev.debuggings.clickgui.elements.Element
import dev.debuggings.clickgui.elements.SubSection
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIRoundedRectangle
import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.pixel
import gg.essential.elementa.dsl.toConstraint
import gg.essential.elementa.effects.ScissorEffect

class Section(val name: String) : UIRoundedRectangle(4F) {

    private var isDragging: Boolean = false
    private var dragOffset: Pair<Float, Float> = 0f to 0f

    val elements: ArrayList<Element<*>> = ArrayList()

    var clickGui: ClickGui? = null

    fun addElement(element: Element<*>) {
        element.clickGui = this.clickGui
        element.section = this
        elements.add(element)
        this.addChild(element)
    }

    fun updateHeight(doSecond: Boolean = true) {
        if (getHeight() == 28.0F) {
            if (doSecond) children[1].setHeight(20.pixel())
            var height = 0F

            children.forEachIndexed { index, child ->
                if (index == 0) return@forEachIndexed
                height += child.getHeight() + 1
            }

            setHeight((28 + height).pixel())
            updatePositions()
        } else {
            elements.forEach {
                if (it is SubSection) {
                    it.setHeight(100.pixel())
                    it.updateHeight()
                    it.saveValue()
                }
            }

            setHeight(28.pixel())

            if (doSecond) children[1].setHeight(0.pixel())
        }
    }

    fun updatePositions() {
        var height = 25F

        children.forEachIndexed { index, comp ->
            if (index == 0) return@forEachIndexed
            comp.setY(height.pixel())
            height += comp.getHeight() + 1
        }
    }

    fun init() {
        elements.forEach {
            it.init()
        }

        updatePositions()

        val x = clickGui?.config?.get<Double>("$name.state.X")?.toFloat() ?: getLeft()
        val y = clickGui?.config?.get<Double>("$name.state.Y")?.toFloat() ?: getTop()
        val collapsed = clickGui?.config?.get<Boolean>("$name.state.collapsed") ?: true

        setX(x.pixel())
        setY(y.pixel())

        if (collapsed) {
            children[1].setHeight(0.pixel())
            setHeight(28.pixel())
        } else {
            children[1].setHeight(20.pixel())

            var height = 0F

            children.forEachIndexed { index, child ->
                if (index == 0) return@forEachIndexed
                height += child.getHeight() + 1
            }

            setHeight((28 + height).pixel())
        }

        saveValue()
    }

    private fun saveValue() {
        clickGui!!.config.set<Double>("$name.state.X", getLeft().toDouble())
        clickGui!!.config.set<Double>("$name.state.Y", getTop().toDouble())
        clickGui!!.config.set<Boolean>("$name.state.collapsed", getHeight() == 28F)
        clickGui!!.config.save()
    }

    private val titleBar = UIBlock(Colors.TITLE).constrain {
        x = 0.pixel()
        y = 4.pixel()
        width = 100.pixel()
        height = 20.pixel()
    }

    init {
        constrain {
            x = CenterConstraint()
            y = CenterConstraint()
            width = 100.pixel()
            height = 28.pixel()
            color = Colors.BACKGROUND.toConstraint()

            enableEffect(ScissorEffect())
        }

        titleBar.onMouseClick { event ->
            if (event.mouseButton == 0) {
                isDragging = true

                dragOffset = event.absoluteX to event.absoluteY
            }
            else if (event.mouseButton == 1) {
                updatePositions()
                updateHeight()
                saveValue()
            }
        }.onMouseRelease {
            isDragging = false
        }.onMouseDrag { mouseX, mouseY, _ ->
            if (!isDragging) return@onMouseDrag

            val absoluteX = mouseX + getLeft()
            val absoluteY = mouseY + getTop()

            val deltaX = absoluteX - dragOffset.first
            val deltaY = absoluteY - dragOffset.second

            dragOffset = absoluteX to absoluteY

            val newX = this@Section.getLeft() + deltaX
            val newY = this@Section.getTop() + deltaY

            this@Section.setX(newX.pixel())
            this@Section.setY(newY.pixel())

            saveValue()
        } childOf this

        UIText(name).constrain {
            x = 4.pixel()
            y = CenterConstraint()
            color = Colors.TITLE_TEXT.toConstraint()
        } childOf titleBar
    }

    infix fun sectionOf(clickGui: ClickGui) = apply {
        clickGui.addSection(this)
    }
}
