package com.example.javafilmoratekotlin.parsing

import com.example.javafilmoratekotlin.controllers.FilmController
import org.springframework.stereotype.Component
import java.lang.reflect.Method

@Component
class MethodParser(private val classParser: ClassParser) {

    fun extractClassInfo(methods: List<Method>): List<MethodView>? {
        return methods.map{extractMethodInfo(it, classParser)}
    }

    private fun extractMethodInfo(method: Method, clazzParser: ClassParser): MethodView {
        val operation = io.swagger.v3.oas.annotations.Operation::class.java
        return MethodView(
            name = method.name,
            path = "",
            description = method.getAnnotation(operation).description,
            summary = method.getAnnotation(operation).summary,
            parameters = null,
            result = null
        )
    }

//    private fun inputParameters(method: Method): List<ClassView>{
//        val inputs = method.parameters.toList()
//        for(i in inputs){
//
//        }
//        classParser.typeSeparator
//
//
//    }
}

data class MethodView(
    val name: String,
    // POST /films
    val path: String,
    val description: String?,
    val summary: String?,
    val parameters: List<ClassView>?,
    val result: ClassView?
)
