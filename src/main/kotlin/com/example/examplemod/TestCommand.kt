package com.example.examplemod

import gg.essential.api.EssentialAPI
import gg.essential.api.commands.Command
import gg.essential.api.commands.DefaultHandler
import gg.essential.api.commands.SubCommand

class TestCommand : Command("clickguidebug") {

    @DefaultHandler
    fun handle() {
        val gui = TestClickGui()
        gui.clickGui.init()
        EssentialAPI.getGuiUtil().openScreen(gui.clickGui)
    }

    @SubCommand("dsl")
    fun dslGui() {
        EssentialAPI.getGuiUtil().openScreen(TestDslClickGui)
    }
}
