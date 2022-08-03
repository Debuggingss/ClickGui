package com.example.examplemod

import dev.debuggings.clickgui.ClickGui
import dev.debuggings.clickgui.Section
import dev.debuggings.clickgui.elements.*
import gg.essential.universal.UChat
import java.awt.Color

class TestClickGui {

    val clickGui = ClickGui(
        "./config/testclickgui.toml",
    )

    private val section = Section("Section") sectionOf clickGui

    private val subSection = SubSection(
        "Sub Section",
        defaultValue = true,
        saveState = true,
        allowBinding = true,
        toggleFunctionality = true
    ) subSectionOf section

    private val subSection2 = SubSection(
        "Sub Section 2",
        defaultValue = true,
        saveState = true,
        allowBinding = true,
        toggleFunctionality = true
    ) subSectionOf subSection

    val button = ButtonElement(
        "Button",
        allowBinding = true
    ) {
        UChat.chat("I should work, if not, I don't.")
    } elementOf subSection2

    val color = ColorPickerElement(
        "Color Picker",
        Color(255, 0, 0)
    ) elementOf subSection2

    val decimalSlider = DecimalSliderElement(
        "Decimal Slider",
        0.1F,
        10F,
        2F
    ) elementOf subSection2

    val secureToggle = SecureToggleElement(
        "Secure Toggle",
        false
    ) elementOf subSection2

    val select = SelectElement(
        "Select",
        "Pog",
        arrayListOf("Hello", "World", "Pog")
    ) elementOf subSection2

    val separator = DividerElement().setHeight(3f) elementOf subSection2

    val slider = SliderElement(
        "Slider",
        1,
        10,
        2
    ) elementOf subSection2

    val textInput = TextInputElement(
        "Text Input",
        "I am a text input"
    ) elementOf subSection2

    val toggle = ToggleElement(
        "Toggle",
        defaultValue = true,
        saveState = true,
        allowBinding = true,
    ) elementOf subSection2
}
