plugins {
    id("maven-publish")
    id("org.jetbrains.kotlin.jvm")
    id("gg.essential.multi-version")
    id("gg.essential.loom")
    id("gg.essential.defaults")
}

group = "dev.debuggings"
version = "1.0.0"

loom {
    runConfigs {
        named("client") {
            ideConfigGenerated(true)
        }
    }

    launchConfigs {
        getByName("client") {
            arg("--tweakClass", "gg.essential.loader.stage0.EssentialSetupTweaker")
        }
    }
}

val embed by configurations.creating
configurations.implementation.get().extendsFrom(embed)

repositories {
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("gg.essential:essential-$platform:4167+g4594ad6e6")
    embed("gg.essential:loader-launchwrapper:1.1.3")
    embed("com.electronwill.night-config:toml:3.6.5")
}

tasks.jar {
    from(embed.files.map {
        zipTree(it)
    })

    exclude("com/example/examplemod/**")
    exclude("gg/essential/**")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "dev.debuggings"
            artifactId = "clickgui"

            from(components["java"])
        }
    }
}
