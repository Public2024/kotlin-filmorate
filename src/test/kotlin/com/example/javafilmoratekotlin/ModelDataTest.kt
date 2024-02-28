package com.example.javafilmoratekotlin

import com.example.javafilmoratekotlin.controllers.FilmController
import com.example.javafilmoratekotlin.model.Film
import com.example.javafilmoratekotlin.model.Genre
import com.example.javafilmoratekotlin.parsing.ClassParser
import com.example.javafilmoratekotlin.parsing.InputParameter
import com.example.javafilmoratekotlin.parsing.TypeField
import com.example.javafilmoratekotlin.util.TypeSeparator
import com.fasterxml.jackson.module.kotlin.isKotlinClass
import io.swagger.v3.oas.annotations.media.Schema
import org.assertj.core.internal.bytebuddy.description.type.TypeDescription
import org.junit.jupiter.api.Test
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.lang.reflect.Parameter
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.time.LocalDate
import java.util.Collections
import kotlin.reflect.KClass
import kotlin.reflect.full.allSupertypes
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.defaultType
import kotlin.reflect.javaType
import kotlin.reflect.jvm.internal.impl.protobuf.WireFormat.FieldType
import kotlin.reflect.jvm.kotlinProperty
import kotlin.reflect.typeOf


class ModelDataTest {

    private val parser = ClassParser()



    @Test
    fun test1() {
        fun extractAnnotationsScheme(clazz: Class<*>, name: String): Schema? {
            val field = clazz.declaredFields.find { it.name.equals(name) }?.annotations?.find { it is Schema } as? Schema
            val fieldConstruct =
                clazz.constructors[0].parameters.find { it.name.equals(name) }?.annotations?.find { it is Schema } as? Schema
            return field ?: fieldConstruct
        }

    }




}