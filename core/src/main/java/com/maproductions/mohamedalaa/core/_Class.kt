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

import java.lang.reflect.Field
import java.lang.reflect.Modifier
import kotlin.reflect.KClass
import android.os.Build

internal fun Class<*>.declaredFieldsForSuperclassesOnly(initialList: List<Field> = emptyList()): List<Field> {
    // ignore interfaces isa. ( has no baking fields isa. )
    val superclass = superclass
    val list = (superclass ?: return initialList).declaredFields.filterNotNull()
    return superclass.declaredFieldsForSuperclassesOnly(initialList + list)
}

internal fun Class<*>.getDeclaredFieldByNameSearchingSelfAndSuperclasses(name: String): Field? {
    val allFields = declaredFieldsForSuperclassesOnly(declaredFields.filterNotNull())
    return allFields.firstOrNull {
        it.name == name
    }
}

@Suppress("unused")
internal fun Class<*>.getDeclaredFieldByNameSearchingSuperclassesOnly(name: String): Field? {
    val allFields = declaredFieldsForSuperclassesOnly()
    return allFields.firstOrNull {
        it.name == name
    }
}

/**
 * - Takes about less than 100 ms the first time invoked.
 *
 * - Subsequent calls of this fun takes less than 5 ms isa.
 *
 * - Should be better than [Class.kotlin] + [KClass.objectInstance] as it's first invocation
 * takes about 1 sec (1000 ms).
 *
 * - Note I don't know if this might make an error in future of either not detecting
 * an object declaration as one or vice versa.
 */
@PublishedApi
internal fun <E> Class<E>.objectInstance(): E? {
    // Check is declared in kotlin code, otherwise not an object declaration isa,
    val doesNotHaveMetaDataAnnotation = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        getDeclaredAnnotation(Metadata::class.java) == null
    }else {
        declaredAnnotations.any { it is Metadata }.not()
    }
    // Also check single private constructor isa.
    if (doesNotHaveMetaDataAnnotation
        || declaredConstructors.size != 1
        || Modifier.isPrivate(declaredConstructors.first().modifiers).not()) {
        return null
    }

    // Check instance of class existence isa.
    val field = fields.firstOrNull { it.name == "INSTANCE" } ?: return null

    // Check instance modifiers isa.
    return if (Modifier.isPublic(field.modifiers)
        && Modifier.isStatic(field.modifiers)
        && Modifier.isFinal(field.modifiers)) {
        @Suppress("UNCHECKED_CAST")
        field.get(null) as? E
    }else {
        null
    }
}
