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

package com.maproductions.mohamedalaa.core.internal

import com.maproductions.mohamedalaa.core.checkTrue
import java.lang.reflect.*

/**
 * ## Goal
 *
 * - Converts [Type], [ParameterizedType], [GenericArrayType], [WildcardType] & [TypeVariable]
 * to/from [String] isa.
 */
internal object MATypes {

    fun typeToString(type: Type): String {
        return if (type is Class<*> && type.isArray.not()) {
            type.name
        }else {
            type.toString()
        }
    }

    /**
     * @param string must be a result from [typeToString] fun isa.
     */
    fun stringToType(string: String): Type {
        return when {
            string.endsWith(">") -> {
                if (string.endsWith("<>")) {
                    val stringOfRawType = string.substring(0, string.length - 2)

                    val rawType = stringToType(stringOfRawType)

                    ParameterizedTypeImpl.create(
                        (rawType as? Class<*>)?.enclosingClass,
                        rawType,
                        emptyArray()
                    )
                }else {
                    val indexOfFirstBracket = string.indexOf("<")

                    val stringOfRawType = string.substring(0, indexOfFirstBracket)

                    val rawType = stringToType(stringOfRawType)

                    // Without first brackets <> isa.
                    val totalStringOfParams = string.substring(indexOfFirstBracket.inc(), string.length.dec())

                    // split , not inside <> -> then -> use stringToType for each argument isa.
                    var bracketsCount = 0
                    var index = 0
                    val charArray = charArrayOf('<', '>', ',')
                    val listOfCommasIndices = mutableListOf(-1)
                    while (true) {
                        index = totalStringOfParams.indexOfAny(charArray, startIndex = index)

                        if (index == -1) break

                        when (totalStringOfParams[index]) {
                            '<' -> bracketsCount++
                            '>' -> bracketsCount--
                            ',' -> if (bracketsCount == 0) {
                                listOfCommasIndices += index
                            }
                        }
                    }
                    listOfCommasIndices += totalStringOfParams.length

                    val actualTypeArguments = listOfCommasIndices.zipWithNext().map { (startIndex, endIndex) ->
                        val stringOfType = totalStringOfParams.substring(startIndex.inc(), endIndex).trim()

                        stringToType(stringOfType)
                    }

                    ParameterizedTypeImpl.create(
                        (rawType as? Class<*>)?.enclosingClass,
                        rawType,
                        actualTypeArguments.toTypedArray()
                    )
                }
            }
            string.endsWith("[]") -> {
                val stringOfComponentType = string.substring(0, string.length - 2)

                val componentType = stringToType(stringOfComponentType)

                GenericArrayTypeImpl.create(componentType)
            }
            string.startsWith("?") -> {
                when {
                    string == "?" -> {
                        WildcardTypeImpl.create(arrayOf(java.lang.Object::class.java), emptyArray())
                    }
                    string.startsWith("? super ") -> {
                        val lowerBound = stringToType(string.substringAfter("? super "))

                        WildcardTypeImpl.create(arrayOf(java.lang.Object::class.java), arrayOf(lowerBound))
                    }
                    else -> {
                        val upperBound = stringToType(string.substringAfter("? extends "))

                        WildcardTypeImpl.create(arrayOf(upperBound), emptyArray())
                    }
                }
            }
            string.startsWith("#") -> {
                val array = string.split("#")
                val nameOnCode = array[0]
                val stringOfGenericDeclaration = array[1]
                // Ignore prefix and suffix of <> isa.
                val stringOfParams = array[2].let { it.substring(1, it.length.dec()) }

                if (stringOfParams.isEmpty()) {
                    TypeVariableImpl.create(
                        emptyArray(),
                        Class.forName(stringOfGenericDeclaration),
                        nameOnCode
                    )
                }else {
                    // split , not inside <> -> then -> use stringToType for each argument isa.
                    var bracketsCount = 0
                    var index = 0
                    val charArray = charArrayOf('<', '>', ',')
                    val listOfCommasIndices = mutableListOf(-1)
                    while (true) {
                        index = stringOfParams.indexOfAny(charArray, startIndex = index)

                        if (index == -1) break

                        when (stringOfParams[index]) {
                            '<' -> bracketsCount++
                            '>' -> bracketsCount--
                            ',' -> if (bracketsCount == 0) {
                                listOfCommasIndices += index
                            }
                        }
                    }
                    listOfCommasIndices += stringOfParams.length

                    val typeParams = listOfCommasIndices.zipWithNext().map { (startIndex, endIndex) ->
                        val stringOfType = stringOfParams.substring(startIndex.inc(), endIndex).trim()

                        stringToType(stringOfType)
                    }

                    TypeVariableImpl.create(
                        typeParams.toTypedArray(),
                        Class.forName(stringOfGenericDeclaration),
                        nameOnCode
                    )
                }
            }
            else -> {
                Class.forName(string)
            }
        }
    }

    fun canonicalize(type: Type): Type = when(type) {
        is Class<*> -> {
            if (type.isArray) GenericArrayTypeImpl.create(type.componentType!!) else type
        }
        is ParameterizedType -> {
            ParameterizedTypeImpl.create(
                type.ownerType,
                type.rawType,
                type.actualTypeArguments
            )
        }
        is GenericArrayType -> {
            GenericArrayTypeImpl.create(type.genericComponentType)
        }
        is WildcardType -> {
            WildcardTypeImpl.create(type.upperBounds, type.lowerBounds)
        }
        is TypeVariable<*> -> {
            TypeVariableImpl.create(type.bounds, type.genericDeclaration, type.name)
        }
        else -> throw RuntimeException("Can\'t canonicalize type -> $type")
    }

    private fun Type.checkNotPrimitive() {
        if (this is Class<*> && this.isPrimitive) {
            throw RuntimeException("Primitive as type argument in a parameterized type")
        }
    }

    /**
     * @param componentType type of items in the array isa.
     */
    private data class GenericArrayTypeImpl(private val componentType: Type) : GenericArrayType {

        companion object {
            fun create(componentType: Type): GenericArrayTypeImpl {
                return GenericArrayTypeImpl(canonicalize(componentType))
            }
        }

        override fun getGenericComponentType(): Type = componentType

        override fun toString(): String = "$componentType[]"

    }

    /**
     * @param ownerType the parent/owner type, if this type ([rawType]) is an inner type,
     * otherwise `null` is returned if this ([rawType]) is a top-level type.
     *
     * @param rawType the declaring type of this parameterized type, Ex. The raw type of
     * [Set]<[String]> is [Set].
     *
     * @param actualTypeArguments an array of the actual type arguments for this type.
     */
    private data class ParameterizedTypeImpl(
        private val ownerType: Type?,
        private val rawType: Type,
        private val actualTypeArguments: Array<Type>,
    ) : ParameterizedType {

        companion object {
            fun create(
                ownerType: Type?,
                rawType: Type,
                actualTypeArgs: Array<Type>
            ): ParameterizedTypeImpl {
                return ParameterizedTypeImpl(
                    ownerType?.let { canonicalize(it) },
                    canonicalize(rawType),
                    Array(actualTypeArgs.size) {
                        val type = actualTypeArgs[it]
                        type.checkNotPrimitive()
                        canonicalize(type)
                    },
                )
            }
        }

        init {
            checkParentType()
        }

        private fun checkParentType() {
            if (rawType is Class<*>) {
                val isStaticOrTopLevelClass = Modifier.isStatic(rawType.modifiers)
                        || rawType.enclosingClass == null

                checkTrue(ownerType != null || isStaticOrTopLevelClass)
            }
        }

        override fun getRawType(): Type = rawType

        override fun getOwnerType(): Type? = ownerType

        override fun getActualTypeArguments(): Array<Type> = actualTypeArguments

        override fun toString(): String {
            if (actualTypeArguments.isEmpty()) {
                return "${typeToString(rawType)}<>"
            }

            val builder = StringBuilder()
            builder.append(typeToString(rawType))
                .append(
                    actualTypeArguments.joinToString(", ", prefix = "<", postfix = ">") { typeToString(it) }
                )

            return builder.toString()
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is ParameterizedTypeImpl) return false

            if (ownerType != other.ownerType) return false
            if (rawType != other.rawType) return false
            if (!actualTypeArguments.contentEquals(other.actualTypeArguments)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = ownerType?.hashCode() ?: 0
            result = 31 * result + rawType.hashCode()
            result = 31 * result + actualTypeArguments.contentHashCode()
            return result
        }

    }

    /**
     * - Upper bound are bounds due to extending a class or implementing an interface isa.
     *
     * - extends == out
     *
     * - Only 1 or none lower bound isa.
     *
     * - Lower-bound is when you specify (? super Field) means argument can be any Field or
     * superclass of Field, and a class can have a single superclass only isa.
     */
    private data class WildcardTypeImpl(
        private val upperBound: Type,
        private val lowerBound: Type?,
    ) : WildcardType {

        companion object {
            fun create(upperBounds: Array<Type>, lowerBounds: Array<Type>): WildcardTypeImpl {
                checkTrue(upperBounds.size <= 1)
                checkTrue(lowerBounds.size <= 1)

                return if (lowerBounds.size == 1) {
                    WildcardTypeImpl(
                        java.lang.Object::class.java,
                        canonicalize(lowerBounds.first())
                    )
                }else {
                    WildcardTypeImpl(
                        upperBounds.firstOrNull()?.let { canonicalize(it) } ?: java.lang.Object::class.java,
                        null
                    )
                }
            }
        }

        override fun getUpperBounds(): Array<Type> = arrayOf(upperBound)

        override fun getLowerBounds(): Array<Type> = if (lowerBound == null) emptyArray() else arrayOf(lowerBound)

        override fun toString(): String {
            return when {
                lowerBound != null -> "? super ${typeToString(lowerBound)}"
                upperBound == java.lang.Object::class.java -> "?"
                else -> "? extends ${typeToString(upperBound)}"
            }
        }

    }

    private data class TypeVariableImpl <G : GenericDeclaration> (
        private val typeParams: Array<Type>,
        private val genericDeclaration: G,
        private val nameOnCode: String
    ) : TypeVariable<G> {

        companion object {
            fun <G : GenericDeclaration> create(
                typeParams: Array<Type>,
                genericDeclaration: G,
                nameOnCode: String
            ): TypeVariableImpl<G> {
                return TypeVariableImpl(
                    Array(typeParams.size) {
                        canonicalize(typeParams[it])
                    },
                    genericDeclaration,
                    nameOnCode
                )
            }
        }

        override fun getBounds(): Array<Type> = typeParams

        override fun getGenericDeclaration(): G = genericDeclaration

        override fun getName(): String = nameOnCode

        override fun toString(): String {
            val params = typeParams.joinToString(", ", prefix = "<", postfix = ">") {
                typeToString(it)
            }

            return "#$nameOnCode#${(genericDeclaration as Class<*>).name}#$params"
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is TypeVariableImpl<*>) return false

            if (!typeParams.contentEquals(other.typeParams)) return false
            if (genericDeclaration != other.genericDeclaration) return false
            if (nameOnCode != other.nameOnCode) return false

            return true
        }

        override fun hashCode(): Int {
            var result = typeParams.contentHashCode()
            result = 31 * result + genericDeclaration.hashCode()
            result = 31 * result + nameOnCode.hashCode()
            return result
        }

    }

}
