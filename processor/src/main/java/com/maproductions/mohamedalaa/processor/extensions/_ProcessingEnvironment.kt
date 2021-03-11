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

package com.maproductions.mohamedalaa.processor.extensions

import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.tools.Diagnostic

fun ProcessingEnvironment.error(msg: Any?) {
    messager.printMessage(
        Diagnostic.Kind.ERROR,
        msg?.toString() ?: "NULL"
    )
}

fun ProcessingEnvironment.warning(msg: Any?) {
    messager.printMessage(
        Diagnostic.Kind.WARNING,
        msg?.toString() ?: "NULL",
    )
}

fun ProcessingEnvironment.warning(msg: Any?, element: Element) {
    messager.printMessage(
        Diagnostic.Kind.WARNING,
        msg?.toString() ?: "NULL",
        element
    )
}

fun ProcessingEnvironment.note(msg: Any?) {
    messager.printMessage(
        Diagnostic.Kind.NOTE,
        msg?.toString() ?: "NULL"
    )
}

fun ProcessingEnvironment.noteSafely(msg: Any?, errorAction: (Throwable) -> Unit = {}) {
    kotlin.runCatching {
        note(msg)
    }.getOrElse {
        errorAction(it)
    }
}
