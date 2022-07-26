package com.example.examplemod

import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent

@Mod(
    modid = ExampleMod.MOD_ID,
    name = ExampleMod.MOD_NAME,
    version = ExampleMod.VERSION
)
class ExampleMod {

    companion object {
        const val MOD_ID = "examplemod"
        const val MOD_NAME = "ExampleMod"
        const val VERSION = "1.0"
    }

    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
        TestCommand().register()
    }
}
