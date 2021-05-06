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

import com.maproductions.mohamedalaa.coloredconsole.*
import com.maproductions.mohamedalaa.core.internal.getFullTypeInfo
import com.maproductions.mohamedalaa.sample.core.del_later.P1KT
import org.junit.Test
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.lang.reflect.TypeVariable
import java.lang.reflect.WildcardType

@Suppress("LocalVariableName")
class TestTypes {

    interface I1
    interface I2
    interface I3
    open class O00
    open class O0 : O00()
    open class O1 : O0(), I1
    open class O2 : O1(), I2, I3
    class O3: O2()

    // List<? extends Foo>

    class C3 <K> {
        var f1: TP1<in TP1<in TP1<in K>>>? = null
        var f2: TP1<out TP1<out TP1<out K>>>? = null
        var f3: TP1<in TP1<out TP1<out K>>>? = null
        var f4: TP1<out TP1<out TP1<in K>>>? = null
    }

    @Test
    fun f1234560() {
        val fields = C3<O2>().javaClass.declaredFields

        for (field in fields) {
            consoleVerboseLn(field.genericType)
            consoleVerboseLn(field.genericType.javaClass)

            val type = (field.genericType as ParameterizedType).actualTypeArguments.first()
            consoleVerboseLn(type.javaClass)
            consoleVerboseLn((type as WildcardType).upperBounds.toList())
            consoleVerboseLn(type.lowerBounds.toList())

            consoleVerboseLn("")
        }
    }

    abstract class Out1<T>(private val string: String) {
        abstract fun get(): T
        abstract fun set(t: T)
    }

    class C2 <K> where K : O1, K : I2, K : I3 {

        var p1: TP1<in K>? = null
        var p2: TP1<out K>? = null

    }

    @Test
    fun f123456() {
        val fields = C2<O2>().javaClass.declaredFields

        val f0 = fields[0]
        val f1 = fields[1]

        consoleVerboseLn(f0.genericType)
        consoleVerboseLn(f0.genericType.javaClass)
        val t1 = (f0.genericType as ParameterizedType).actualTypeArguments.first()
        consoleVerboseLn(t1.javaClass)
        consoleVerboseLn((t1 as WildcardType).upperBounds.toList())
        consoleVerboseLn((t1 as WildcardType).lowerBounds.toList())

        consoleVerboseLn(f1.genericType.javaClass)
        val t2 = (f1.genericType as ParameterizedType).actualTypeArguments.first()
        consoleVerboseLn(t2.javaClass)
        consoleVerboseLn((t2 as WildcardType).upperBounds.toList())
        consoleVerboseLn((t2 as WildcardType).lowerBounds.toList())
    }

    class C1 <B : A<E>, T : B, E> {
        var p1: T? = null
        var p2: E? = null
        var p3: List<*/*TypesImplAbstractClass1 learn in and out tany isa.*/>? = null
        var p4: TP1<in String>? = null
        var p5: TP1<out String>? = null
    }

    class TP1<A>

    /*
    public List<? extends String> s = null;
        public List<? super String> s2 = null;
     */

    @Test
    fun f12345() {
        val fields = C1<M1, M1, String>().javaClass.declaredFields

        val f4 = fields[3]
        val f5 = fields[4]

        consoleVerboseLn(f4.genericType.javaClass)
        val t1 = (f4.genericType as ParameterizedType).actualTypeArguments.first()
        consoleVerboseLn(t1.javaClass)
        consoleVerboseLn((t1 as WildcardType).upperBounds.toList())
        consoleVerboseLn((t1 as WildcardType).lowerBounds.toList())

        consoleVerboseLn(f5.genericType.javaClass)
        val t2 = (f5.genericType as ParameterizedType).actualTypeArguments.first()
        consoleVerboseLn(t2.javaClass)
        consoleVerboseLn((t2 as WildcardType).upperBounds.toList())
        consoleVerboseLn((t2 as WildcardType).lowerBounds.toList())
    }

    @Test
    fun f1234() {
        val (f1, f2, f3) = C1<M1, M1, String>().javaClass.declaredFields.let { Triple(it[0], it[1], it[2]) }

        consoleVerboseLn(f3.genericType.javaClass)
        val t1 = (f3.genericType as ParameterizedType).actualTypeArguments.first()
        consoleVerboseLn(t1.javaClass)
    }

    @Test
    fun f123() {
        val (f1, f2) = C1<M1, M1, String>().javaClass.declaredFields.let { it[0] to it[1] }

        consoleDebugLn(f1.genericType)
        consoleDebugLn(f1.genericType.javaClass)
        consoleDebugLn(f1.genericType is WildcardType)
        consoleDebugLn(f1.genericType is ParameterizedType)
        consoleDebugLn((f1.genericType as? TypeVariable<*>)?.name)
        consoleDebugLn((f1.genericType as? TypeVariable<*>)?.genericDeclaration)
        consoleDebugLn((f1.genericType as? TypeVariable<*>)?.bounds?.toList()?.map { it to (it.javaClass to (it as? ParameterizedType)?.rawType) })

        consoleWarnLn(f2.genericType)
        consoleWarnLn(f2.genericType.javaClass)
        consoleWarnLn(f2.genericType is WildcardType)
        consoleWarnLn(f2.genericType is ParameterizedType)
    }

    abstract class A<I> {

    }

    class M1 : A<String>()

    class SimpleClass {
        val triple1 = Triple(
            4 to listOf(3.2),
            "",
            listOf(4 to 5) to listOf("" to " ")
        )
    }

    class StrangeProperties1<T> {
        var v1: T? = 8 as? T
    }

    class StrangeProperties2<T : TypesAbstractClass1> {
        var v1: T? = TypesImplAbstractClass1() as? T
    }

    data class Tmp1(var p1: P1KT.I1<Int> = P1KT.I1(), var p2: P1KT.I2<Int> = P1KT().I2())

    @Test
    fun a() {
        val type1: Type = P1KT.I1<Int>().javaClass
        val type2: Type = P1KT().I2<Int>().javaClass
        val tmp1 = Tmp1()

        consoleDebugLn(type1 as? ParameterizedType)
        consoleDebugLn(type2 as? ParameterizedType)
        consoleErrorLn(tmp1.javaClass.declaredFields.first().genericType.javaClass)
        consoleWarnLn((tmp1.javaClass.declaredFields.first().genericType as? ParameterizedType)?.ownerType)
        consoleInfoLn(tmp1.p1.javaClass)
        consoleInfoLn(tmp1.p1.javaClass.enclosingClass)
        consoleInfoLn(tmp1.javaClass.enclosingClass)
        consoleInfoLn(tmp1.javaClass.enclosingClass?.enclosingClass)
        consoleWarnLn((tmp1.javaClass.declaredFields.first().genericType as? ParameterizedType)?.rawType)
        consoleWarnLn((tmp1.javaClass.declaredFields.first().genericType as? ParameterizedType)?.actualTypeArguments?.toList())
        consoleWarnLn((tmp1.javaClass.declaredFields[1].genericType as? ParameterizedType)?.ownerType)
    }

    @Test
    fun parameterized_type_2() {
        if (true) {
            consoleDebugLn(intArrayOf(3).javaClass)
            consoleDebugLn(intArrayOf(3).javaClass.componentType)
            consoleDebugLn(intArrayOf(3).javaClass.componentType.isPrimitive)
            consoleDebugLn(arrayOf(3).javaClass.componentType)
            consoleDebugLn(arrayOf(3).javaClass.componentType.isPrimitive)
            consoleDebugLn(intArrayOf(3).javaClass.interfaces.toList())

            return
        }

        val s1 = StrangeProperties1<Int>()
        val s2 = StrangeProperties2<TypesImplAbstractClass1>()
        val s3 = StrangeProperties2<TypesImplAbstractClass2>()

        consoleVerboseLn(s1.javaClass.declaredFields.first().genericType)
        consoleVerboseLn(s1.javaClass.typeParameters.first().name)
        consoleVerboseLn(s1.javaClass.typeParameters.first().bounds.toList())

        s1.p1()
        consoleWarnLn(s1.getFullTypeInfo())
    }

    private inline fun <reified R> R.p1() {
        consoleErrorLn(R::class)
        consoleErrorLn(R::class.java)
        consoleErrorLn(this.getFullTypeInfo())
    }

    @Test
    fun parameterized_type() {
        val simpleClass = SimpleClass()

        val f1 = simpleClass.javaClass.declaredFields.first { it.name == SimpleClass::triple1.name }

        // : T how to get T except from a type variable of the classssss sa7 kda isa.
        consoleErrorLn(f1.genericType)
        consoleErrorLn(f1.type)
        consoleErrorLn(f1.type as? ParameterizedType)
        f1.isAccessible = true
        consoleErrorLn(f1.get(simpleClass).javaClass)
        consoleErrorLn(f1.get(simpleClass).javaClass as? ParameterizedType)
        consoleWarnLn(f1.get(simpleClass).javaClass.getFullTypeInfo())
        consoleWarnLn(f1.get(simpleClass).getFullTypeInfo())
        consoleWTFLn(f1.get(simpleClass).p1())
        consoleWTFLn(f1.get(simpleClass).javaClass.p1())
        consoleWarnLn(f1.get(simpleClass).getFullTypeInfo() as? ParameterizedType)
        val type = f1.genericType as ParameterizedType
        consoleVerboseLn(type.ownerType, stacktraceLimit = 1)
        consoleVerboseLn(type.rawType, stacktraceLimit = 1)
        consoleVerboseLn(type.actualTypeArguments.toList(), stacktraceLimit = 1)
    }

}
