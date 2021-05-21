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
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java-library")

    id("kotlin")

    id("com.github.dcendents.android-maven")
}

group = Groups.github

dependencies {
    implementation(fileTree(dirLibsIncludeJar()))

    implementation(project(Deps.own_libs.annotation))

    implementation(Deps.kotlin.stdlib_jdk8)

    implementation(Deps.squareup.kotlinpoet)
    implementation(Deps.squareup.kotlinpoet_metadata)
    implementation(Deps.squareup.kotlinpoet_metadata_specs)

    implementation(Deps.kotlinx.metadata)

    implementation(Deps.org_reflection.reflections)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = Versions.jvm_1_8
    }
}
