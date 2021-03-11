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

@file:Suppress("unused", "SameParameterValue")

package com.maproductions.mohamedalaa.dependencies

/**
 * - GroupId, ArtifactId, Version
 */
object Deps {

    val kotlin = Kotlin

    val own_libs = OwnLibs

    val androidx = Androidx()

    val kotlinx = Kotlinx

    const val gson = "com.google.code.gson:gson:${Versions.gson}"

    const val kotlin_poet = "com.squareup:kotlinpoet:${Versions.kotlin_poet}"

    // ---- Testing ---- //

    const val junit = "junit:junit:${Versions.test_junit}"

    const val robolectric = "org.robolectric:robolectric:${Versions.test_robolectric}"

    // ---- Groups (might include testing as well isa) ---- //

    object Kotlinx : BaseGroup() {
        override val name: String
            get() = "org.jetbrains.kotlinx"

        val metadata = lib("kotlinx-metadata-jvm", Versions.kotlinx_metadata)
    }

    class Androidx : BaseGroup() {
        override val name: String
            get() = "androidx"

        val test = Test()

        inner class Test : BaseGroup() {
            override val name: String
                get() = "${this@Androidx.name}.test"

            val core = lib("core", Versions.test_androidx_core)
            val runner = lib("runner", Versions.test_androidx_core)
            val rules = lib("rules", Versions.test_androidx_core)

            val junit = lib("junit", Versions.test_androidx_junit)
            val espresso = Espresso()

            inner class Espresso : BaseGroup() {
                override val name: String
                    get() = "${this@Test.name}.espresso"

                val core = lib("espresso-core", Versions.test_androidx_espresso)
            }

        }

    }

    object OwnLibs : EmptyBaseGroup() {
        val annotation = ownLib("annotation")
    }

    object Kotlin : SameVersionBaseGroup() {
        override val name: String
            get() = "org.jetbrains.kotlin"

        override val version: String
            get() = Versions.kotlin

        val stdlib_jdk8 = lib("kotlin-stdlib-jdk8")

        val reflect = lib("kotlin-reflect")

        val test = lib("kotlin-test")
    }

    abstract class BaseGroup {
        protected abstract val name: String

        protected fun lib(artifact: String, version: String): String = "$name:$artifact:$version"

        protected fun ownLib(moduleName: String): String = ":$moduleName"

        override fun toString(): String = name
    }

    abstract class EmptyBaseGroup : BaseGroup() {
        override val name: String
            get() = ""
    }

    abstract class SameVersionBaseGroup : BaseGroup() {
        abstract val version: String

        protected fun lib(artifact: String): String = "$name:$artifact:$version"
    }

}