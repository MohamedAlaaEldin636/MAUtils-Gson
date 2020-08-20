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

import android.os.Build
import androidx.annotation.IntRange
import com.maproductions.lib.flow_1.custom_classes.DataResult
import com.maproductions.lib.mautils_gson_core.fromJson
import com.maproductions.lib.mautils_gson_core.toJson
import com.maproductions.mohamedalaa.core.model.UICountry
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import timber.log.Timber
import kotlin.test.assertEquals

@Suppress("LocalVariableName")
@Config(manifest = Config.NONE, sdk = [Build.VERSION_CODES.P])
@RunWith(RobolectricTestRunner::class)
class GsonComplexConversions {
    
    @Test
    fun checkViaEqualityOfOriginAndGeneratedOne() {
        val d2 = provideUICountryGeoApi()
        val j2 = d2.toJson().apply(Timber::e)
        val r2: UICountry? = j2.fromJson()

        assertEquals(r2, d2)

        val d1 = provideSuccessString()
        val j1 = d1.toJson().apply(Timber::e)
        val r1: DataResult<String>? = j1.fromJson()

        assertEquals(r1, d1)

        val data1 = provideSuccessGeoApi()
        val data2 = provideSuccessRestApi()

        val error0 = provideError(0)
        val error1 = provideError(1)
        val error2 = provideError(2)

        val load = provideLoading()

        val j_data1 = data1.toJson().apply(Timber::e)
        val j_data2 = data2.toJson().apply(Timber::e)

        val j_error0 = error0.toJson()
        val j_error1 = error1.toJson()
        val j_error2 = error2.toJson()

        val j_load = load.toJson()

        val r_j_data1: DataResult<UICountry> = j_data1.fromJson()
        val r_j_data2: DataResult<UICountry> = j_data2.fromJson()

        val r_j_load: DataResult<UICountry> = j_load.fromJson()

        assertEquals(r_j_data1, data1)
        assertEquals(r_j_data2, data2)

        assertEquals(r_j_load, load)

        val r_j_error0: DataResult<UICountry> = j_error0.fromJson()
        val r_j_error1: DataResult<UICountry> = j_error1.fromJson()
        val r_j_error2: DataResult<UICountry> = j_error2.fromJson()

        assertEquals(r_j_error0, error0)
        assertEquals(r_j_error1, error1)
        assertEquals(r_j_error2, error2)
    }

    @Test
    fun withTypeParams() {
        val v1: DataResult<UICountry> = provideDataSuccess2()

        val j1 = v1.toJson()

        val r1 = j1.fromJson<DataResult<UICountry>>()

        assertEquals(v1, r1)
    }

    private fun provideDataSuccess2() = DataResult.Success2(
        provideUICountryGeoApi(),
        listOf(
            Pair(
                3.2f to 9.54,
                22,
            ),
        ),
    )

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

    private fun provideError(@IntRange(from = 0, to = 2) index: Int): DataResult<UICountry> {
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
