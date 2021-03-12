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

import com.maproductions.mohamedalaa.dependencies.*

plugins {
    id("com.android.library")

    kotlin("android")

    kotlin("kapt")
}

group = Groups.github_sample

android {

    compileSdkVersion(Versions.compile_sdk)
    buildToolsVersion(Versions.build_tools)

    defaultConfig {
        minSdkVersion(Versions.min_sdk)
        targetSdkVersion(Versions.target_sdk)

        versionCode = Versions.code
        versionName = Versions.name

        // Used to use VectorDrawableCompat in XML isa.
        vectorDrawables.useSupportLibrary = true

        testInstrumentationRunner = Const.test_instrumentation_runner
        consumerProguardFiles(Const.consumer_proguard_files)
    }

    buildTypes {
        release {
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
        jvmTarget = Versions.jvm_1_8
    }
}

dependencies {
    api(project(Deps.own_libs.core))

    api(project(Deps.own_libs.annotation))

    testImplementation(project(Deps.own_libs.core, Const.integrate_test_implementations))
    androidTestImplementation(project(Deps.own_libs.core, Const.integrate_android_test_implementations))
}
