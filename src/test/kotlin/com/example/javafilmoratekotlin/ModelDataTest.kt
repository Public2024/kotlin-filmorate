package com.example.javafilmoratekotlin


import com.example.javafilmoratekotlin.parsing.ClassParser
import io.swagger.v3.oas.annotations.media.Schema
import org.junit.jupiter.api.Test


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