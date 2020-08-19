package com.maproductions.lib.mautils_gson_core_processor.extensions;

import com.squareup.kotlinpoet.ClassName;
import com.squareup.kotlinpoet.ParameterizedTypeName;
import com.squareup.kotlinpoet.TypeName;

import org.jetbrains.annotations.NotNull;

public class KotlinpoetUtils {

    @NotNull
    public static TypeName parameterizedTypeName(ClassName receiver, TypeName... typeParams) {
        return ParameterizedTypeName.get(receiver, typeParams);
    }

}
