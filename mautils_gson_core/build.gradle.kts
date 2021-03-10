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

import com.maproductions.mohamedalaa.dependencies.Versions

plugins {
    id("com.android.library")

    kotlin("android")

    kotlin("kapt")

    // Jitpack
    id("com.github.dcendents.android-maven")
}

group = Config.Group.github

android {

    compileSdkVersion(Versions.compile_sdk)
    buildToolsVersion(Versions.build_tools)

    defaultConfig {
        minSdkVersion(Config.Android.minSdk)
        targetSdkVersion(Config.Android.targetSdk)

        versionCode = Config.Android.versionCode
        versionName = Config.Android.versionName

        // Used to use VectorDrawableCompat in XML isa.
        vectorDrawables.useSupportLibrary = true

        testInstrumentationRunner = Config.Android.androidJUnitRunner
        consumerProguardFiles(Config.Android.consumerRulesPro)
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    // Project Modules
    api(project(Config.ProjectModules.mautils_gson_core_annotation)) // Can be excluded

    api(Config.Libs.kotlin_reflect)

    api(Config.Libs.gson)

    testImplementation(project(Config.ProjectModules.core_1, "integrateTestImplementations"))
    androidTestImplementation(project(Config.ProjectModules.core_1, "integrateAndroidTestImplementations"))
}
