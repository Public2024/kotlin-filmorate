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
    fun test() {
        println(parser.extractClassInfo(FilmController::class.java))
    }

    @Test
    fun test1() {
        fun extractAnnotationsScheme(clazz: Class<*>, name: String): Schema? {
            val field = clazz.declaredFields.find { it.name.equals(name) }?.annotations?.find { it is Schema } as? Schema
            val fieldConstruct =
                clazz.constructors[0].parameters.find { it.name.equals(name) }?.annotations?.find { it is Schema } as? Schema
            return field ?: fieldConstruct
        }

    }

    @Test
    fun test3() {

        val field = FilmController::class.java.declaredFields.find { it.name.equals("id") }

        fun extractAnnotationsScheme(field: Field?): Schema? {
            var annotationSchema = field?.annotations?.find { it is Schema } as? Schema
            if (annotationSchema == null) {
                val valueConstructor = field?.declaringClass?.constructors
                var n = 0
                if (valueConstructor != null) {
                    while (n < valueConstructor.size) {
                        val value = field.declaringClass?.constructors?.get(n)?.parameters?.find { it.name.equals(field.name) }
                        val schema = value?.annotations?.find { it is Schema } as? Schema
                        if (schema != null) {
                            annotationSchema = schema
                            break
                        }
                        n += 1
                    }
                }
            }
            return annotationSchema
        }

        println(extractAnnotationsScheme(field)?.description)

    }



    class Generic<T : Any>(val klass: Class<T>) {
        companion object {
            inline operator fun <reified T : Any>invoke() = Generic(T::class.java)
        }

        fun checkType(t: Any) {
            when {
                klass.isAssignableFrom(t.javaClass) -> println("Correct type")
                else -> println("Wrong type")
            }

        }
    }

    inline fun <reified T : Any> classOfList(list: List<T>): KClass<T> = T::class

    @Test
    fun testTypeCollection(){

        val typeSeparator = TypeSeparator()

        val field = Film::class.java.declaredFields.toList()[5]

        val parameter = FilmController::class.java.declaredMethods.find { it.name.equals("createFilm") }?.parameters?.get(2)

        fun test(clazz: Class<*>)  {
            if(typeSeparator.isPresent(clazz))
                TypeField.PRIMITIVE
            if (typeSeparator.isCollection(clazz))
//                println(clazz::class.allSupertypes)
                println(clazz)


        }

        Generic<String>().checkType(field)







    }


}