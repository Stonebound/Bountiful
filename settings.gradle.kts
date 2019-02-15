
pluginManagement {
    repositories {
        maven {
            name = "forge"
            url = uri("https://files.minecraftforge.net/maven")
        }
        gradlePluginPortal()
    }
    resolutionStrategy {
        eachPlugin {
            println("REQ: ${requested.id.id}")
            if (requested.id.id.startsWith("org.jetbrains.kotlin")) useVersion("1.3.10")
            when (requested.id.id) {
                "net.minecraftforge.gradle" -> useModule("net.minecraftforge.gradle:ForgeGradle:3.0.101")
            }
        }
    }
}