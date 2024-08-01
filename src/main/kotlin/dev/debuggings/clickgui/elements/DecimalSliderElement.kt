package dev.debuggings.clickgui.elements

import dev.debuggings.clickgui.Colors
import dev.debuggings.clickgui.Utils
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIRoundedRectangle
import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.pixel
import gg.essential.elementa.dsl.toConstraint
import kotlin.math.floor

class DecimalSliderElement @JvmOverloads constructor(
    name: String,
    private val minValue: Float,
    private val maxValue: Float,
    private val defaultValue: Float,
    override var description: String? = null,
) : Element<Float>(name, defaultValue, description) {

    private var isDragging: Boolean = false

    override fun loadValue() {
        value = clickGui!!.config.get<Double>(savePath)?.toFloat() ?: defaultValue
        sliderBox.setX(((90F / (maxValue - minValue)) * (value - minValue) + 3).pixel())
        valueText.setText(value.toString())
    }

    override fun saveValue() {
        clickGui!!.config.set<Double>(savePath, value.toDouble())
        clickGui!!.config.save()
    }

    override var nameText: UIText? = UIText(name).constrain {
        x = 5.pixel()
        y = CenterConstraint()
        textScale = 0.5.pixel()
        color = Colors.OPTION_TEXT.toConstraint()
    } childOf this

    private val valueText = UIText(value.toString()).constrain {
        x = 5.pixel(true)
        y = CenterConstraint()
        textScale = 0.5.pixel()
        color = Colors.ON.toConstraint()
    } childOf this

    private val sliderBar = UIBlock(Colors.OPTION_TEXT).constrain {
        x = 5.pixel()
        y = 14.pixel()
        width = 90.pixel()
        height = 2.pixel()
    } childOf this

    private val sliderBox = UIRoundedRectangle(3F).constrain {
        x = 3.pixel()
        y = 11.pixel()
        width = 4.pixel()
        height = 8.pixel()
        color = Colors.ON.toConstraint()
    } childOf this

    private val sliderMinText = UIText(minValue.toString()).constrain {
        x = 3.pixel()
        y = 2.pixel()
        textScale = 0.5.pixel()
        color = Colors.OPTION_TEXT.toConstraint()
    } childOf this

    private val sliderMaxText = UIText(maxValue.toString()).constrain {
        x = 3.pixel(true)
        y = 2.pixel()
        textScale = 0.5.pixel()
        color = Colors.OPTION_TEXT.toConstraint()
    } childOf this

    override fun init() {
        loadValue()

        sliderBar.hide()
        sliderBox.hide()
        sliderMinText.hide()
        sliderMaxText.hide()

        onMouseClick {
            isDragging = true
        }

        onMouseRelease {
            isDragging = false
        }

        onMouseDrag { mouseX, _, _ ->
            if (!isDragging) return@onMouseDrag

            val x = mouseX.coerceIn(5F, 95F) - 5

            val sliderX = (90F / (maxValue * 10 - minValue * 10)) *
                    Utils.map(x.toInt(), 0, 90, 0, (maxValue * 10 - minValue * 10).toInt())

            sliderBox.setX((sliderX + 3).pixel())

            value = floor(Utils.mapFloat(x, 0F, 90F, minValue, maxValue) * 10) / 10
            valueText.setText(value.toString())

            saveValue()
        }

        onMouseEnter {
            nameText?.hide()

            sliderBox.unhide()
            sliderBar.unhide()
            sliderMinText.unhide()
            sliderMaxText.unhide()

            valueText.setX(CenterConstraint())
            valueText.setY(2.pixel())
        }

        onMouseLeave {
            nameText?.unhide()

            sliderBox.hide()
            sliderBar.hide()
            sliderMinText.hide()
            sliderMaxText.hide()

            valueText.setX(5.pixel(true))
            valueText.setY(CenterConstraint())
        }
    }
}
