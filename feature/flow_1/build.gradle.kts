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

plugins {
    id(Config.Plugins.androidLibrary)

    kotlin(Config.Plugins.kotlinAndroid)

    kotlin(Config.Plugins.kotlinAndroidExtensions)

    kotlin(Config.Plugins.kotlinKapt)
}

android {
    compileSdkVersion(Config.Android.compileSdk)
    buildToolsVersion(Config.Android.builtTools)

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

    buildFeatures {
        dataBinding = true
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
    api(project(Config.ProjectModules.core_2))

    implementation(project(Config.ProjectModules.mautils_gson_core_annotation))

    testImplementation(project(Config.ProjectModules.core_1, "integrateTestImplementations"))
    androidTestImplementation(project(Config.ProjectModules.core_1, "integrateAndroidTestImplementations"))
}
