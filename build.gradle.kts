/*
 * Copyright © 2020 Mohamed Alaa
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

// Top-level build file where you can add configuration options common to all sub-projects/modules.

//import org.gradle.kotlin.dsl.`kotlin-dsl`

buildscript {

    /*plugins {
        //`kotlin-dsl`
        //id("libs.gradle.kts")
    }*/
    apply(from = "libs.gradle.kts")

    repositories {
        google()
        jcenter()
    }
    
    dependencies {
        classpath(Config.BuildPlugins.androidGradle)

        classpath(Config.BuildPlugins.kotlinGradlePlugin)

        classpath(Config.BuildPlugins.jitpack)
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
