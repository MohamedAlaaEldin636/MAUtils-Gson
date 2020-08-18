// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    
    val kotlin_version by extra("1.4.0")
    repositories {
        google()
        jcenter()
    }
    
    dependencies {
        classpath(Config.BuildPlugins.androidGradle)

        classpath(Config.BuildPlugins.kotlinGradlePlugin)
        "classpath"("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
    }

}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
