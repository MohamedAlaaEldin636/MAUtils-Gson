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

package com.maproductions.mohamedalaa.core

import android.content.Intent
import android.os.Bundle
import com.google.gson.Gson

/**
 * - Same as [Bundle.putJson] isa.
 */
fun Intent.putJson(key: String?, value: Any?, gson: Gson? = null) {
    putExtras(Bundle().also { it.putJson(key, value, gson) })
}

/**
 * - Same as [Bundle.getJsonOrNull] isa.
 */
inline fun <reified T> Intent.getJsonOrNull(key: String?, gson: Gson? = null): T? {
    return extras?.getJsonOrNull<T>(key, gson)
}

/**
 * - Same as [Bundle.getJson] isa.
 *
 * @throws [RuntimeException] if [Intent.getExtras] is `null` isa.
 */
inline fun <reified T> Intent.getJson(key: String?, gson: Gson? = null): T {
    return extras?.getJson<T>(key, gson) ?: throw RuntimeException("extras in Intent is `null` isa.")
}
