package com.example.javafilmoratekotlin

import com.example.javafilmoratekotlin.model.Film
import com.example.javafilmoratekotlin.parsing.ClassEnumView
import com.example.javafilmoratekotlin.parsing.ClassParser
import io.swagger.v3.oas.annotations.media.Schema
import org.junit.jupiter.api.Test



class ModelDataTest {

    private val parser = ClassParser()

    @Test
    fun test() {
        println(parser.extractClassInfo(Film::class.java))
    }

    @Test
    fun test1() {
        val fields = Film::class.java.constructors[0].parameters
        println(fields)
        for (f in fields) {
            println((f.annotations.find { it is Schema } as? Schema)?.description)
        }
    }


    @Test
    fun test3() {

        fun extractAnnotationsScheme(clazz: Class<*>, name: String): Schema? {
            val field = clazz.declaredFields.find { it.name.equals(name) }?.annotations?.find { it is Schema } as? Schema
            val fieldConstruct = clazz.constructors[0].parameters.find { it.name.equals(name) }?.annotations?.find { it is Schema } as? Schema
            return field ?: fieldConstruct

        }

        println(extractAnnotationsScheme(Film::class.java, "duration")?.description)
    }
}