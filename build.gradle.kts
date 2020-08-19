// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    
    repositories {
        google()
        jcenter()
    }
    
    dependencies {
        classpath(Config.BuildPlugins.androidGradle)

        classpath(Config.BuildPlugins.kotlinGradlePlugin)
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
