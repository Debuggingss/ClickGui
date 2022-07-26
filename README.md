# ClickGui
An easy to use ClickGui library built with Elementa

## Usage
1) Go to [JitPack](https://jitpack.io/#Debuggingss/ClickGui/) and do what it says to add ClickGui to your build.gradle.
2) Make a class for your ClickGui config. Here is an example:
```kt
class ClickGuiConfig {

    val clickGui = ClickGui(FileConfig.of("./config/mymod.toml"))

    private val section = Section("Section") sectionOf clickGui

    // Sub sections are sections inside other sections, they are infinitely stackable meaning
    // you can put a subsection inside a subsection inside a subsection inside a...
    private val subSection = SubSection(
        "Sub Section",                          // Name
        true,                                   // Default Value
        true,                                   // Save Value
        true,                                   // Allow Binding
        true                                    // Whether this subsection should act as a toggle as well
                                                // Only the first argument is required if this is set to false
    ) subSectionOf section

    // The button element allows you to run a function
    val button = ButtonElement(
        "Button",                               // Name
        allowBinding = true                     // Whether you can bind a hotkey to it
    ) {                                         // The function
        Minecraft.getMinecraft().thePlayer.addChatMessage(
            ChatComponentText("I should work, if not, I don't.")
        )
    } elementOf subSection

    // The color picker allows you to enter any hex color code and read it as a java.awt.Color
    val color = ColorPickerElement(
        "Color Picker",                         // Name
        Color(255, 0, 0)                        // Default Value
    ) elementOf subSection
    
    // The decimal slider allows you to set a float
    val decimalSlider = DecimalSliderElement(
        "Decimal Slider",                       // Name
        0.1F,                                   // Minimum Value
        10F,                                    // Maximum Value
        2F                                      // Default Value
    ) elementOf subSection
    
    // The secure toggle element requires you to click it 3 times to change it's value
    // and also does not allow binding keys to it
    val secureToggle = SecureToggleElement(
        "Secure Toggle",                        // Name
        false                                   // Default Value
    ) elementOf subSection

    // The select element allows you to select a specific string from an array and return it as a String
    val select = SelectElement(
        "Secure Toggle",                        // Name
        "Pog",                                  // Default Value, must be in the options array
        arrayListOf("Hello", "World", "Pog")    // The options
    ) elementOf subSection
    
    // The separator, well, separates. yes. DO NOT TRY TO READ IT'S VALUE!!!
    val separator = SeparatorElement("Separator") elementOf subSection

    // The slider element allows you to set an integer
    val slider = SliderElement(
        "Slider",                               // Name
        1,                                      // Minimum Value
        10,                                     // Maximum Value
        2                                       // Default Value
    ) elementOf subSection
    
    // The text input element allows you to set a string, surprising innit?
    val textInput = TextInputElement(
        "Text Input",                           // Name
        "I am a text input"                     // Default Value
    )
    
    // The toggle element is just a switch
    val toggle = ToggleElement(
        "Toggle",                               // Name
        true,                                   // Default Value
        true,                                   // Save Value
        true,                                   // Allow Binding
    ) elementOf subSection
}
```
3. Once you are done writing your gigantic bloated class, head over to your mod's main file and create a variable in your companion object:
```kt
var config: ClickGuiConfig? = null
```
4. Go to your init method (FMLInitializationEvent) and add the following:
```kt
// This is important because if we were to do this in the companion object, the config couldn't load in time and everything would break.
config = ClickGuiConfig()
config!!.clickGui.init()
```
5. Finally, create a way to open your GUI, either by a keybind, a command, or voice control:
```kt
EssentialAPI.getGuiUtil().openScreen(MyMod.config?.clickGui)
```
