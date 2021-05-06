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

package com.maproductions.mohamedalaa.sample.core

import android.app.Application
import android.content.pm.ApplicationInfo
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.maproductions.mohamedalaa.coloredconsole.*
import com.maproductions.mohamedalaa.core.*
import com.maproductions.mohamedalaa.core.toJson
import com.maproductions.mohamedalaa.sample.core.normal_gson_same_field_name.OpenClass2
import org.junit.Test
import org.junit.runner.RunWith
import org.objenesis.ObjenesisStd
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import kotlin.jvm.internal.FunctionReference
import kotlin.jvm.internal.ReflectionFactory
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.primaryConstructor

@Suppress("LocalVariableName")
@Config(manifest = Config.NONE, sdk = [Build.VERSION_CODES.P])
@RunWith(RobolectricTestRunner::class)
class NormalGsonConversion {

    private fun Gson.toJsonOrExceptionAsString(src: Any?): String? {
        return kotlin.runCatching { toJson(src) }.getOrElse { "Exception -> $it" }
    }

    /*private */open class O1(var a: Int)
    /*private */open class O2(var b: Float) : O1(b.toInt())

    private val generalGson by lazy {
        GsonBuilder()
            .serializeNulls()
            .setLenient()
            .enableComplexMapKeySerialization()
            /*.setFieldNamingStrategy {
                "${it.declaringClass}\$${it.name}"
            }*/
            .create()
    }

    class M1(i: Int) {

        init {
            consolePrintLn("HI")

            if (i == 0) throw RuntimeException("aAAAAAAAAAAAAAAAAAAAAAAAAAAA")
        }

        var s = i.toString()
    }
    /*private */class M2(i: Int, o2: O2) {
        var i2 = i

        var o1 = O1(o2.b.toInt().inc())
    }

    @Test
    fun wrong2() {
        val objenesis = ObjenesisStd()

        val objectInstantiator = objenesis.getInstantiatorOf(M1::class.java)

        val m1 = objectInstantiator.newInstance()

        consoleWarnLn(m1.s)

        m1.s = "new value"
        consoleInfoLn(m1.s)

        // todo ezan even the no args do not use it unless above fails or throw excep balash el no args isa.
        // BUT
        /*
        To improve performance, it is best to reuse the ObjectInstantiator objects as much as possible.
        For example, if you are instantiating multiple instances of a specific class, do it from the same ObjectInstantiator.

        Both InstantiatorStrategy and ObjectInstantiator can be shared between multiple threads and used concurrently. They are thread safe.

        ================

        So

        maybe make a map get if not exist and put obj instantiator to get class bs kda isa. map of 3 class and obj inst kda ya3ne isa.

        ObjenesisStd already haave cache so have static lazy ObjenesisStd instance and keep getting instantiators isa.
         */

        // todo succeeded isa, but try to use ct lib directly instead of the github which puts others isa.
    }

    @Test
    fun wrong1() {
        // test with m1 to see if init works with
        // https://stackoverflow.com/questions/4133709/is-it-possible-in-java-to-create-blank-instance-of-class-without-no-arg-constr
        // isa todo
        //sun.reflect.ReflectionFactory.getReflectionFactory
        //val vzz = ReflectionFactory().function(FunctionReference(0, )).call()

        consoleErrorLn(M1::class.primaryConstructor!!.call(0))

        val m1 = M1(3)
        val m2 = M2(7, O2(65.3f))

        m1.s = "hello"
        m2.i2 = 65
        m2.o1 = O1(959595)

        val j1 = generalGson.toJson(m1)
        val j2 = generalGson.toJson(m2)
        val r1 = generalGson.fromJson(j1, M1::class.java)
        val r2 = generalGson.fromJson(j2, M2::class.java)

        val v = M2::class
        consoleErrorLn(kotlin.runCatching { v.createInstance() }.getOrElse { it })
        consoleErrorLn(kotlin.runCatching { v.java.newInstance() }.getOrElse { it })
        consoleWarnLn(kotlin.runCatching { v.primaryConstructor?.call(null, null) }.getOrElse { it })
        consoleWarnLn(kotlin.runCatching { v.primaryConstructor?.call(34, O2(4f)) }.getOrElse { it })
        consoleWarnLn(kotlin.runCatching { v.primaryConstructor?.call(m2, null, null) }.getOrElse { it })
        // sol is for parameters put any values like 0 for int and same recusrion if custom obj
        // then loo through fields and put data isa.

        consoleDebugLn(r1.s)
        consoleDebugLn(r2.i2)
        consoleDebugLn(r2.o1.a)
    }

    @Test
    fun superFieldsSupport() {
        val o2 = O2(3.toFloat())

        o2.b = 45f
        o2.a = 90

        val j1 = generalGson.toJson(o2)
        val r1 = generalGson.fromJson(j1, O2::class.java)

        consoleDebugLn(r1.a)
        consoleDebugLn(r1.b)
    }

    @Test
    fun sameSuperClassFieldName() {
        val gson1 = GsonBuilder()
            .serializeNulls()
            .setLenient()
            .enableComplexMapKeySerialization()
            .setFieldNamingStrategy {
                "${it.declaringClass}\$${it.name}"
            }
            .create()

        val gson2 = GsonBuilder()
            .serializeNulls()
            .setLenient()
            .enableComplexMapKeySerialization()
            .create()

        val o1 = OpenClass2()

        println(
            gson1.toJsonOrExceptionAsString(o1)
        )
        // todo make a library for consoleLog colors isa.
        //gson1.fieldNamingStrategy().translateName()
        println(
            gson2.toJsonOrExceptionAsString(o1)
        )
    }

    @Test // sameFieldNameInSuperclass // AndType
    fun check1() {
        /*val gson = GsonBuilder()
            .serializeNulls()
            .setLenient()
            .enableComplexMapKeySerialization()
            .create()*/

        //RuntimeEnvironment.systemContext
        //ApplicationInfo.
        println(ApplicationProvider.getApplicationContext<Application>())

        val openClass2 = OpenClass2()

        val j1 = openClass2.toJsonOrNull()

        val v1 = j1.fromJsonOrNull<OpenClass2>()

        println(j1)
        println(v1)
        println(openClass2 == v1)
        println(openClass2 === v1)
    }

}
