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

package com.maproductions.lib.mautilsgson

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.IntRange
import com.maproductions.lib.flow_1.custom_classes.DataResult
import com.maproductions.lib.mautils_gson_core.fromJson
import com.maproductions.lib.mautils_gson_core.toJson
import com.maproductions.mohamedalaa.core.model.UICountry
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dataResultAsGson()
    }

    @Suppress("LocalVariableName")
    fun dataResultAsGson() {
        val d2 = a1()
        val j2 = d2.toJson().apply(Timber::e)
        val r2: UICountry? = j2.fromJson()

        Timber.e(specialAssertion(r2, d2))

        val d1 = provideSuccess1()
        val j1 = d1.toJson().apply(Timber::e)
        val r1: DataResult<String>? = j1.fromJson()

        Timber.e(specialAssertion(r1, d1))

        val data1 = provideSuccessGeoApi()
        val data2 = provideSuccessRestApi()

        val error0 = provideError(0)
        val error1 = provideError(1)
        val error2 = provideError(2)
        val error3 = provideError(3)
        val error4 = provideError(4)
        val error5 = provideError(5)

        val load = provideLoading()

        val j_data1 = data1.toJson().apply(Timber::e)
        val j_data2 = data2.toJson().apply(Timber::e)

        val j_error0 = error0.toJson()
        val j_error1 = error1.toJson()
        val j_error2 = error2.toJson()
        val j_error3 = error3.toJson()
        val j_error4 = error4.toJson()
        val j_error5 = error5.toJson()

        val j_load = load.toJson()

        val r_j_data1: DataResult<UICountry> = j_data1.fromJson()
        val r_j_data2: DataResult<UICountry> = j_data2.fromJson()

        val r_j_load: DataResult<UICountry> = j_load.fromJson()

        Timber.e(specialAssertion(r_j_data1, data1))
        Timber.e(specialAssertion(r_j_data2, data2))

        Timber.e(specialAssertion(r_j_load, load))

        val r_j_error0: DataResult<UICountry> = j_error0.fromJson()
        val r_j_error1: DataResult<UICountry> = j_error1.fromJson()
        val r_j_error2: DataResult<UICountry> = j_error2.fromJson()
        val r_j_error3: DataResult<UICountry> = j_error3.fromJson()
        val r_j_error4: DataResult<UICountry> = j_error4.fromJson()
        val r_j_error5: DataResult<UICountry> = j_error5.fromJson()

        Timber.e(specialAssertion(r_j_error0, error0))
        Timber.e(specialAssertion(r_j_error1, error1))
        Timber.e(specialAssertion(r_j_error2, error2))
        Timber.e(specialAssertion(r_j_error3, error3))
        Timber.e(specialAssertion(r_j_error4, error4))
        Timber.e(specialAssertion(r_j_error5, error5))
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

    private fun provideSuccess1(): DataResult<String> {
        return DataResult.Success("A")
    }

    private fun a1(): UICountry {
        return (provideSuccessGeoApi() as DataResult.Success).value
    }

}