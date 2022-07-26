package com.example.examplemod

import gg.essential.api.EssentialAPI
import gg.essential.api.commands.Command
import gg.essential.api.commands.DefaultHandler

class TestCommand : Command("clickguidebug") {

    @DefaultHandler
    fun handle() {
        val gui = TestClickGui()
        gui.clickGui.init()
        EssentialAPI.getGuiUtil().openScreen(gui.clickGui)
    }
}
