import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id(Config.Plugins.javaOrKotlinLibrary)

    id(Config.Plugins.kotlin)
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(project(Config.ProjectModules.mautils_gson_core_annotation))

    implementation(Config.Libs.kotlin_stdlib)
    implementation(Config.Libs.kotlin_reflect)

    implementation(Config.Libs.java_poet)
    implementation(Config.Libs.kotlin_poet)

    implementation("io.github.classgraph:classgraph:4.8.87")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()
