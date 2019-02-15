import net.minecraftforge.gradle.patcher.PatcherExtension
import org.gradle.language.jvm.tasks.ProcessResources
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import net.minecraftforge.gradle.userdev.UserDevExtension

val v_forge = "1.12.2-14.23.5.2768"
val v_forgelin = "1.8.2"
val v_jei = "4.11.0.206"
val v_minecraft_root = "1.12"
val v_minecraft = "$v_minecraft_root.2"

val v_gamestages = "2.0.105"
val v_bookshelf = "2.3.557"

buildscript {
    repositories {
        jcenter()
        mavenCentral()
        maven("https://files.minecraftforge.net/maven")
    }
    dependencies {

    }
}


plugins {
    java
    idea
    kotlin("jvm")  version "1.3.10"
    id("net.minecraftforge.gradle")
}


val bouVersion: String
    get() {
        val versionFile = File("src/main/kotlin/ejektaflex/bountiful/BountifulInfo.kt")
        val gotVersion = if (versionFile.exists()) {
            versionFile.readLines().find { "VERSION" in it }?.dropWhile { it != '"' }?.drop(1)?.dropLast(1) ?: "UNDEFINED"
        } else {
            "UNDEFINED"
        }
        println("build.gradle.kts got Bountiful version: $gotVersion")
        return gotVersion
    }

version = bouVersion

base {
    archivesBaseName = "Bountiful"
}


java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

val minecraft: UserDevExtension by extensions
minecraft.apply {
    //setUseDepAts(true)
    version = v_forge
    //runDir = "run"
    mappings = "stable_39"
}

repositories {
    jcenter()
    // Forgelin
    maven("http://maven.shadowfacts.net/")
    // Maven for JEI
    maven("http://dvs1.progwml6.com/files/maven")
    // CurseForge Maven
    maven("https://minecraft.curseforge.com/api/maven/")
    // MMD Maven
    maven("https://maven.mcmoddev.com/")
}

dependencies {
    subprojects.forEach { compile(it) }
    api("net.shadowfacts:Forgelin:$v_forgelin")
    //deobfProvided("mezz.jei:jei_$v_minecraft:$v_jei:api")
    //runtime("mezz.jei:jei_$v_minecraft:$v_jei")

    compileOnly(files("libonly/GameStages-$v_minecraft-$v_gamestages-deobf.jar"))
    compileOnly("net.darkhax.bookshelf:Bookshelf-$v_minecraft:$v_bookshelf:deobf")

    compile(kotlin("stdlib", "1.3.10"))
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<ProcessResources> {
    from(sourceSets["main"].resources.srcDirs) {
        include("mcmod.info")
        expand(mapOf("version" to project.version, "mcversion" to v_forge))
    }
    from(sourceSets["main"].resources.srcDirs) {
        exclude("mcmod.info")
    }
}

// This part may be messy
tasks.withType<Jar> {
    //from(sourceSets["main"].output)
    //println("names")
    //println(sourceSets.names)
    //from(sourceSets["api"].output.classesDirs)
}

tasks.register<Jar>("apiJar") {
    //classifier = "api"
    //from(sourceSets["api"].output)
    //from(sourceSets["api"].allJava)
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}

val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}