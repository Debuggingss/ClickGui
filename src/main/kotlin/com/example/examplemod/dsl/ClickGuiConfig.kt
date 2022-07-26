package com.example.examplemod.dsl

import com.electronwill.nightconfig.core.file.FileConfig
import dev.debuggings.clickgui.ClickGui
import dev.debuggings.clickgui.elements.ButtonElement
import dev.debuggings.clickgui.elements.SecureToggleElement
import gg.essential.universal.UChat
import java.awt.Color

object ClickGuiConfig : ClickGui(FileConfig.of("./config/dsl_gui.toml")) {
    lateinit var toggleElement: SecureToggleElement

    init {
        section("Section") {
            subsection("Sub Section :o") {
                button("Super deeply nested button") {
                    UChat.chat("ToggleElement is now ${toggleElement.value}")
                }

                colorPicker("Color Picker", Color(255, 0, 0))

                decimalSlider("Decimal Slider", 0.1f, 10f, 2f)

                this@ClickGuiConfig.toggleElement = secureToggle("Secure Toggle", false)

                selector("Selector", "Pog", arrayListOf("Hello", "World", "Pog"))

                separator("Separator")

                textInput("Text Input", "I am a text input")

                toggle("Toggle", defaultValue = true, saveState = true, allowBinding = true)

                subsection("Nested subsection wow!") {
                    colorPicker("Nested color picker!!", Color(50, 100, 200))
                }
            }
            button("Button outside subsection") {
                UChat.chat("This is outside the subsection")
            }
        }

        init()
    }
}
