package com.maproductions.lib.mautilsgson

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.IntRange
import com.maproductions.lib.flow_1.custom_classes.DataResult
import com.maproductions.mohamedalaa.core.model.UICountry
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        runCatching {
            dataResultAsGson()
        }.getOrElse {
            Timber.e("Error $it")
        }
    }

    @Suppress("LocalVariableName")
    fun dataResultAsGson() {
        // todo to continue with processor you have to continue with gson_core 1st isa.
        /*val d2 = a1()
        val j2 = d2.toJson().apply(Timber::e).apply {
            Timber.e("this $this")
        }
        val r2: UICountry? = j2.fromJsonOrNull()

        specialAssertion(r2, d2)

        val d1 = provideSuccess1()
        val j1 = d1.toJson().apply(Timber::e).apply {
            Timber.e("this $this")
        }
        val r1: DataResult<String>? = j1.fromJsonOrNull()

        specialAssertion(r1, d1)

        val data1 = provideSuccessGeoApi()
        val data2 = provideSuccessRestApi()

        val error0 = provideError(0)
        val error1 = provideError(1)
        val error2 = provideError(2)
        val error3 = provideError(3)
        val error4 = provideError(4)
        val error5 = provideError(5)

        val load = provideLoading()

        val j_data1 = data1.toJson()
        val j_data2 = data2.toJson()

        val j_error0 = error0.toJson()
        val j_error1 = error1.toJson()
        val j_error2 = error2.toJson()
        val j_error3 = error3.toJson()
        val j_error4 = error4.toJson()
        val j_error5 = error5.toJson()

        val j_load = load.toJson()

        val r_j_data1: DataResult<UICountry> = j_data1.fromJson()
        val r_j_data2: DataResult<UICountry> = j_data2.fromJson()

        val r_j_error0: DataResult<UICountry> = j_error0.fromJson()
        val r_j_error1: DataResult<UICountry> = j_error1.fromJson()
        val r_j_error2: DataResult<UICountry> = j_error2.fromJson()
        val r_j_error3: DataResult<UICountry> = j_error3.fromJson()
        val r_j_error4: DataResult<UICountry> = j_error4.fromJson()
        val r_j_error5: DataResult<UICountry> = j_error5.fromJson()

        val r_j_load: DataResult<UICountry> = j_load.fromJson()

        specialAssertion(r_j_data1, data1)
        specialAssertion(r_j_data2, data2)

        specialAssertion(r_j_error0, error0)
        specialAssertion(r_j_error1, error1)
        specialAssertion(r_j_error2, error2)
        specialAssertion(r_j_error3, error3)
        specialAssertion(r_j_error4, error4)
        specialAssertion(r_j_error5, error5)

        specialAssertion(r_j_load, load)*/
    }

    private fun <T> specialAssertion(expected: T, actual: T) {
        val msg = if (expected == actual) {
            "Succeeded"
        }else {
            "Error\nexpected $expected\nactual $actual"
        }

        Timber.e("Special msg $msg")
    }

    private fun provideSuccessGeoApi(): DataResult<UICountry> {
        return DataResult.Success(
            UICountry.RelatedWithRegionsCountry(
                false,
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
                true,
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