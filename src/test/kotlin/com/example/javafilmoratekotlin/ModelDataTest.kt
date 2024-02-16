package com.example.javafilmoratekotlin

import com.example.javafilmoratekotlin.controllers.FilmController
import com.example.javafilmoratekotlin.model.Film
import com.example.javafilmoratekotlin.model.Genre
import com.example.javafilmoratekotlin.parsing.ClassParser
import com.example.javafilmoratekotlin.parsing.InputParameter
import com.example.javafilmoratekotlin.util.TypeSeparator
import io.swagger.v3.oas.annotations.media.Schema
import org.junit.jupiter.api.Test
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.time.LocalDate



class ModelDataTest {

    private val parser = ClassParser()

    @Test
    fun test() {
        println(parser.extractClassInfo(LocalDate::class.java))
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

        val field = Genre::class.java.declaredFields.find { it.name.equals("id") }

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
}