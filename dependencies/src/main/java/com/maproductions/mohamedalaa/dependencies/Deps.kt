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

@file:Suppress("unused", "SameParameterValue", "PropertyName")

package com.maproductions.mohamedalaa.dependencies

/**
 * - GroupId, ArtifactId, Version
 */
object Deps {

    val kotlin = Kotlin

    val own_libs = OwnLibs()

    val androidx = Androidx()

    val kotlinx = Kotlinx

    val squareup = Squareup

    val org_reflection = OrgReflection

    const val gson = "com.google.code.gson:gson:${Versions.gson}"

    const val material = "com.google.android.material:material:${Versions.material}"

    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"

    const val colored_console = "com.github.MohamedAlaaEldin636:Colored-Console:${Versions.colored_console}"

    // ---- Testing ---- //

    const val junit = "junit:junit:${Versions.test_junit}"

    const val robolectric = "org.robolectric:robolectric:${Versions.test_robolectric}"

    // ---- Groups (might include testing as well isa) ---- //

    object OrgReflection : BaseGroup() {
        override val name: String
            get() = "org.reflections"

        val reflections = lib("reflections", Versions.org_reflections)
    }

    object Squareup : BaseGroup() {
        override val name: String
            get() = "com.squareup"

        val kotlinpoet = lib("kotlinpoet", Versions.kotlin_poet)
        val kotlinpoet_metadata = lib("kotlinpoet-metadata", Versions.kotlin_poet)
        val kotlinpoet_metadata_specs = lib("kotlinpoet-metadata-specs", Versions.kotlin_poet)
    }

    object Kotlinx : BaseGroup() {
        override val name: String
            get() = "org.jetbrains.kotlinx"

        val metadata = lib("kotlinx-metadata-jvm", Versions.kotlinx_metadata)
    }

    class Androidx : BaseGroup() {
        override val name: String
            get() = "androidx"

        val test = Test()

        val annotation = Annotation()

        val core = Core()

        val app_compat = AppCompat()

        val constraint_layout = ConstraintLayout()

        val lifecycle = Lifecycle()

        inner class Lifecycle internal constructor() : SameVersionBaseGroup() {
            override val name: String
                get() = "${this@Androidx.name}.lifecycle"

            override val version: String
                get() = Versions.androidx_lifecycle

            val saved_state = lib("lifecycle-viewmodel-savedstate")
        }

        inner class Core : BaseGroup() {
            override val name: String
                get() = "${this@Androidx.name}.core"

            val core_ktx = lib("core-ktx", Versions.androidx_core)
        }

        inner class AppCompat : SameVersionBaseGroup() {
            override val name: String
                get() = "${this@Androidx.name}.appcompat"

            override val version: String
                get() = Versions.androidx_app_compat

            val app_compat = lib("appcompat")
        }

        inner class ConstraintLayout : SameVersionBaseGroup() {
            override val name: String
                get() = "${this@Androidx.name}.constraintlayout"

            override val version: String
                get() = Versions.androidx_constraint_layout

            val constraint_layout = lib("constraintlayout")
        }

        inner class Test : BaseGroup() {
            override val name: String
                get() = "${this@Androidx.name}.test"

            val core = lib("core", Versions.test_androidx_core)
            val runner = lib("runner", Versions.test_androidx_core)
            val rules = lib("rules", Versions.test_androidx_core)

            val junit = lib("junit", Versions.test_androidx_junit)
            val espresso = Espresso()

            val ext = Ext()

            inner class Espresso : BaseGroup() {
                override val name: String
                    get() = "${this@Test.name}.espresso"

                val core = lib("espresso-core", Versions.test_androidx_espresso)
            }

            inner class Ext : BaseGroup() {
                override val name: String
                    get() = "${this@Test.name}.ext"

                val junit = lib("junit", "1.1.2")
            }

        }

        inner class Annotation : BaseGroup() {
            override val name: String
                get() = "${this@Androidx.name}.annotation"

            val annotation = lib("annotation", Versions.test_androidx_annotation)

        }

    }

    class OwnLibs : EmptyBaseGroup() {
        val core = ownLib("core")
        val annotation = ownLib("annotation")
        val processor = ownLib("processor")

        val sample = Sample()

        inner class Sample : EmptyBaseGroup() {
            val core = ownLib("sample:core")
            val core2 = ownLib("sample:core2")
            val core3 = ownLib("sample:core3")
        }
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