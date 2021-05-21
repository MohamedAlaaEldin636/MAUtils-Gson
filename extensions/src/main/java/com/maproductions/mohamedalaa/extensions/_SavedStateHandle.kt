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

package com.maproductions.mohamedalaa.extensions

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.maproductions.mohamedalaa.core.fromJsonOrNull
import com.maproductions.mohamedalaa.core.toJsonOrNull
import java.lang.ref.WeakReference

inline fun <reified T> SavedStateHandle.getJsonLiveData(
    key: String,
    initialValue: T? = null,
    noinline conversionToJson: (T?) -> String? = {
        it.toJsonOrNull()
    }
): MutableLiveData<T> {
    val json = get<String>(key)

    val toBeUsedInitialValue = if (contains(key)) json.fromJsonOrNull<T>() else initialValue

    return SavedStateMutableLiveData(
        WeakReference(this), key, conversionToJson, toBeUsedInitialValue
    )
}

@PublishedApi
internal class SavedStateMutableLiveData<T>(
    private val weakRefState: WeakReference<SavedStateHandle>,
    private val key: String,
    private val conversionToJson: (T?) -> String?,
    initialValue: T?
) : MutableLiveData<T>(initialValue) {

    override fun setValue(value: T?) {
        saveToSavedStateHandle(value)

        super.setValue(value)
    }

    override fun postValue(value: T?) {
        saveToSavedStateHandle(value)

        super.postValue(value)
    }

    private fun saveToSavedStateHandle(newValue: T?) {
        val json = conversionToJson.invoke(newValue)

        weakRefState.get()?.set(key, json)
    }

}
