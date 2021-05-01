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

@file:Suppress("MemberVisibilityCanBePrivate")

package com.maproductions.mohamedalaa.processor.custom_classes

import java.io.File
import java.io.FileWriter
import java.io.PrintWriter
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.TypeElement

abstract class SpecialAbstractProcessor : AbstractProcessor() {

    protected val printWriter by lazy {
        PrintWriter(
            FileWriter(
                File("F:\\Programs\\Text Editors\\Atom\\Projects\\Programming\\Del Later\\Processor.txt"),
                true
            )
        )
    }

    final override fun process(annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment?): Boolean {
        if (annotations == null || roundEnv == null) return false

        return processAnnotation(annotations, roundEnv).also {
            printWriter.flush()
            printWriter.close()
        }
    }

    protected abstract fun processAnnotation(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean

}
