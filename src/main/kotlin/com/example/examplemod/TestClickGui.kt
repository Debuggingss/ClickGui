package com.example.examplemod

import com.electronwill.nightconfig.core.file.FileConfig
import dev.debuggings.clickgui.ClickGui
import dev.debuggings.clickgui.Section
import dev.debuggings.clickgui.elements.*
import gg.essential.universal.UChat
import java.awt.Color

class TestClickGui {

    val clickGui = ClickGui(
        FileConfig.of("./config/testclickgui.toml"),
    )

    private val section = Section("Section") sectionOf clickGui

    private val subSection = SubSection(
        "Sub Section",
        defaultValue = true,
        saveState = true,
        allowBinding = true,
        toggleFunctionality = true
    ) subSectionOf section

    val button = ButtonElement(
        "Button",
        allowBinding = true
    ) {
        UChat.chat("I should work, if not, I don't.")
    } elementOf subSection

    val color = ColorPickerElement(
        "Color Picker",
        Color(255, 0, 0)
    ) elementOf subSection

    val decimalSlider = DecimalSliderElement(
        "Decimal Slider",
        0.1F,
        10F,
        2F
    ) elementOf subSection

    val secureToggle = SecureToggleElement(
        "Secure Toggle",
        false
    ) elementOf subSection

    val select = SelectElement(
        "Select",
        "Pog",
        arrayListOf("Hello", "World", "Pog")
    ) elementOf subSection

    val separator = SeparatorElement("Separator") elementOf subSection

    val slider = SliderElement(
        "Slider",
        1,
        10,
        2
    ) elementOf subSection

    val textInput = TextInputElement(
        "Text Input",
        "I am a text input"
    ) elementOf subSection

    val toggle = ToggleElement(
        "Toggle",
        defaultValue = true,
        saveState = true,
        allowBinding = true,
    ) elementOf subSection
}
