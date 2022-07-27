plugins {
    kotlin("jvm") version "1.6.10" apply false
    id("gg.essential.loom") version "0.10.0.1" apply false
    id("gg.essential.multi-version.root")
}

preprocess {
    val forge10809 = createNode("1.8.9-forge", 10809, "srg")
}
