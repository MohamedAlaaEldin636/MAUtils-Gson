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

package com.maproductions.mohamedalaa.sample.core.types

import com.maproductions.mohamedalaa.annotation.MAAbstract
import com.maproductions.mohamedalaa.coloredconsole.*
import org.junit.Test
import java.lang.reflect.GenericDeclaration
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

@Suppress("LocalVariableName")
/*@Config(manifest = Config.NONE, sdk = [Build.VERSION_CODES.P])
@RunWith(RobolectricTestRunner::class)*/
class TestTypeVariable {

    @Test
    fun list() {
        val o1 = TypesImplAbstractClass1()
        val f1 = o1.javaClass.declaredFields.first()

        consoleDebugLn(f1.genericType, stacktraceLimit = 1)
        consoleDebugLn(f1.type, stacktraceLimit = 1)
        f1.isAccessible = true
        consoleDebugLn(f1.get(o1).javaClass, stacktraceLimit = 1)

        consoleInfoLn(kotlin.runCatching { Class.forName(f1.genericType.toString()) }.getOrElse { it }, stacktraceLimit = 1)
        consoleInfoLn(kotlin.runCatching { Class.forName(f1.type.toString()) }.getOrElse { it }, stacktraceLimit = 1)
        consoleInfoLn(kotlin.runCatching { Class.forName(f1.get(o1).javaClass.toString()) }.getOrElse { it }, stacktraceLimit = 1)

        consoleWarnLn(kotlin.runCatching { Class.forName("java.util.List") }.getOrElse { it }, stacktraceLimit = 1)

        kotlin.runCatching {
            consoleVerboseLn(f1.type is Class<*>)
            consoleVerboseLn((f1.type as? Class<*>)?.name)
            consoleVerboseLn(Class.forName((f1.type as? Class<*>)?.name ?: ""))

            val type: Type = Class.forName((f1.type as? Class<*>)?.name ?: "")
        }.getOrElse {
            consoleErrorLn(it)
        }

        /*val l = listOf("")

        val clazz = l.javaClass

        consoleDebugLn(clazz, stacktraceLimit = 1)
        consoleDebugLn(clazz, stacktraceLimit = 1)
        consoleDebugLn(Class.forName("kotlin.collections.List"), stacktraceLimit = 1)
        consoleDebugLn(Class.forName("kotlin.collections.List") is ParameterizedType, stacktraceLimit = 1)*/
    }

    @Test
    fun checkWhatItIs() {
        val forTypeVariable = ForTypeVariable<TypesAbstractClass1>(TypesImplAbstractClass1())

        val clazz = forTypeVariable.javaClass // Type

        val tv = clazz.typeParameters.first()

        consoleDebugLn(tv.bounds.toList(), stacktraceLimit = 1)
        consoleDebugLn(tv.genericDeclaration, stacktraceLimit = 1)
        consoleDebugLn(tv.genericDeclaration is Type, stacktraceLimit = 1)
        consoleDebugLn(tv.genericDeclaration is GenericDeclaration, stacktraceLimit = 1)
        consoleDebugLn(tv.genericDeclaration is Class<*>, stacktraceLimit = 1)
        consoleDebugLn(tv.genericDeclaration is ParameterizedType, stacktraceLimit = 1)
        consoleDebugLn(tv.name, stacktraceLimit = 1)

        consoleDebugLn("", stacktraceLimit = 1)

        val field = clazz.declaredFields.first()
        consoleDebugLn(field.type, stacktraceLimit = 1)
        consoleDebugLn(field.genericType, stacktraceLimit = 1)
        field.isAccessible = true
        consoleDebugLn(field.get(forTypeVariable).javaClass, stacktraceLimit = 1)
    }

    class Z1<A, B, C : O2<B>> {
        val a: A? = null
        val b: B? = null
        val c: C? = null
    }
    open class O1
    open class O2<E> : O1()
    //open class O5 : O2<String>()
    open class O3<K, V> : O2<V>()

    @Test
    fun checkWhatItIs3() {
        val z1 = Z1<Int, String, O3<Double, String>>()

        val clazz = z1.javaClass

        val a = clazz.typeParameters[0]
        val b = clazz.typeParameters[1]
        val c = clazz.typeParameters[2]

        consoleDebugLn(c)
        consoleDebugLn(c.bounds.toList())
        consoleDebugLn(c.bounds.toList().first().javaClass)
        consoleDebugLn(c.genericDeclaration.typeParameters.toList())
        consoleDebugLn(c.genericDeclaration.typeParameters.toList().first().genericDeclaration.interfaces.toList())
        consoleDebugLn(c.genericDeclaration.typeParameters[2].genericDeclaration)
    }

    @Test
    fun checkWhatItIs2() {
        val forTypeVariable = ForTypeVariable<TypesAbstractClass1>(TypesImplAbstractClass1())

        val clazz = forTypeVariable.javaClass

        val tv = clazz.typeParameters.first()

        val genericDeclaration = tv.genericDeclaration
        consoleVerboseLn("genericDeclaration $genericDeclaration") // ForTypeVariable

        consoleDebugLn("genericDeclaration.typeParameters.toList() ${genericDeclaration.typeParameters.toList()}") // G

        println()

        consoleWarnLn(genericDeclaration.isAnnotationPresent(MAAbstract::class.java))
        consoleWarnLn(genericDeclaration.getAnnotation(MAAbstract::class.java))
        consoleWarnLn(genericDeclaration.annotations.toList())
        consoleWarnLn(genericDeclaration.getAnnotationsByType(MAAbstract::class.java).toList())
        consoleWarnLn(genericDeclaration.getDeclaredAnnotation(MAAbstract::class.java))
        consoleWarnLn(genericDeclaration.getDeclaredAnnotationsByType(MAAbstract::class.java).toList())
        consoleWarnLn(genericDeclaration.declaredAnnotations.toList())
    }

    @Test
    fun zips() {
        consoleInfoLn(listOf<Int>().joinToString(", ", prefix = "<", postfix = ">") { it.toString() + "ds" })
        consoleInfoLn(listOf<Int>().joinToString(", ", prefix = "<", postfix = ">") { it.toString() + "ds" } == "<>")
        consoleInfoLn(listOf<Int>().joinToString(", ", prefix = "<", postfix = ">") { it.toString() + "ds" }.let { it.substring(1, it.length.dec()) })
        consoleInfoLn(listOf<Int>().joinToString(", ", prefix = "<", postfix = ">") { it.toString() + "ds" }.let { it.substring(1, it.length.dec()) }.isEmpty())

        consoleWarnLn(listOf(2, 3, 4).zipWithNext())
    }


}
