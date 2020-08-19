import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id(Config.Plugins.javaOrKotlinLibrary)

    id(Config.Plugins.kotlin)
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(Config.Libs.kotlin_stdlib)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()
