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

@file:Suppress("unused")

package com.maproductions.mohamedalaa.dependencies

/**
 * - GroupId, ArtifactId, Version
 */
object Deps {

    val kotlin_group = KotlinGroup

    object KotlinGroup : BaseGroup() {
        override val name: String
            get() = "org.jetbrains.kotlin"

        val stdlib_jdk8 = lib("kotlin-stdlib-jdk8", Versions.kotlin)
    }

    abstract class BaseGroup {
        protected abstract val name: String

        protected fun lib(artifact: String, version: String): String = "$name:$artifact:$version"
        protected fun lib(artifact: String, version: Int): String = "$name:$artifact:$version"

        override fun toString(): String = name
    }

}