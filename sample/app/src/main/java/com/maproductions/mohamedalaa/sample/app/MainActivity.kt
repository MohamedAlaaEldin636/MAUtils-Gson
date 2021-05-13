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

package com.maproductions.mohamedalaa.sample.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.IntRange
import com.google.gson.GsonBuilder
import com.maproductions.mohamedalaa.core.*
import com.maproductions.mohamedalaa.sample.core.DataResult
import com.maproductions.mohamedalaa.sample.core.UICountry
import com.maproductions.mohamedalaa.sample.core.normal_gson_same_field_name.AnnDC2
import com.maproductions.mohamedalaa.sample.core.normal_gson_same_field_name.DC1
import com.maproductions.mohamedalaa.sample.core.normal_gson_same_field_name.OpenClass2
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        kotlin.runCatching {
            if (true) {
                val openClass2 = OpenClass2()

                val j2 = DC1("mido")

                val o3 = listOf(
                    5 to Pair(9, j2)
                )

                val gson = GsonBuilder()
                    .serializeNulls()
                    .setLenient()
                    .enableComplexMapKeySerialization()
                    .create()

                Timber.e("gson.toJson(j2) -> ${gson.toJson(j2)}")
                Timber.e("j2 -> $j2")
                Timber.e("j2 -> ${j2.toJsonOrNull()}")
                Timber.e("Ann -> ${AnnDC2("Abo \" Alaa").toJsonOrNull()}")
                Timber.e("o3 -> $o3")
                Timber.e("o3 -> ${o3.toJsonOrNull()}")

                val j1 = openClass2.toJsonOrNull()

                val v1 = j1.fromJsonOrNull<OpenClass2>()

                Timber.e("j1 -> $j1")
                Timber.e(v1?.toString() ?: "NULL")
                Timber.e((openClass2 == v1).toString())
                Timber.e((openClass2 === v1).toString())

                return@runCatching
            }

            smallTests()
        }.getOrElse {
            Timber.e("ERRORRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR\n-> $it")
        }
    }

    @Suppress("LocalVariableName")
    fun smallTests() {
        val d2: UICountry = provideUICountryGeoApi()
        Timber.e(d2.toString())
        val j2 = d2.toJson().apply(Timber::e)
        val r2: UICountry = j2.fromJson()

        Timber.e(specialAssertion(r2, d2))
    }

    private fun <T> specialAssertion(expected: T, actual: T): String {
        val msg = if (expected == actual) {
            "Succeeded"
        }else {
            "Error\nexpected $expected\nactual $actual"
        }

        return "Special msg $msg"
    }

    private fun provideSuccessGeoApi(): DataResult<UICountry> {
        return DataResult.Success(
            UICountry.RelatedWithRegionsCountry(
                //false,
                "code",
                "name",
                "",
                33,
                listOf("1", "2", "3")
            )
        )
    }
    private fun provideSuccessRestApi(): DataResult<UICountry> {
        return DataResult.Success(
            UICountry.SmallInfoCountry(
                //true,
                "a2",
                "a3",
                "name",
                "cap",
                emptyList(),
                listOf("1", "2", "3"),
                listOf(),
                listOf("1", "2", "qq")
            )
        )
    }

    private fun provideError(@IntRange(from = 0, to = 5) index: Int): DataResult<UICountry> {
        return when (index) {
            0 -> DataResult.Error.NoInternetConnection("N")
            1 -> DataResult.Error.Unknown("")
            else -> DataResult.Error.CanNotBeFoundOnServer()
        }
    }
    private fun provideLoading(): DataResult<UICountry> {
        return DataResult.Loading()
    }

    private fun provideSuccessString(): DataResult<String> {
        return DataResult.Success("A")
    }

    private fun provideUICountryGeoApi(): UICountry {
        return (provideSuccessGeoApi() as DataResult.Success).value
    }

}