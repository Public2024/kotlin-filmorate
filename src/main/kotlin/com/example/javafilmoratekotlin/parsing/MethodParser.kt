package com.example.javafilmoratekotlin.parsing

import org.springframework.stereotype.Component
import java.lang.reflect.Method

@Component
class MethodParser(private val classParser: ClassParser) {

    fun extractClassInfo(clazz: Class<*>): List<MethodView>? {
        val methods = clazz.declaredMethods
        return methods.map { extractMethodInfo(it, classParser) }
    }


    fun extractMethodInfo(method: Method, clazzParser: ClassParser): MethodView {
        val operation = io.swagger.v3.oas.annotations.Operation::class.java
        return MethodView(
            name = method.name,
            path = "",
            description = method.getAnnotation(operation).description,
            summary = method.getAnnotation(operation).summary,
            parameters = getAllParameters(method),
            result = parseUniqueParameter(method.returnType)
        )
    }


    private fun getAllParameters(method: Method): List<InputParameter> {
        val parameters = method.parameters
        return parameters.map {
            return@map InputParameter(
                name = it.name,
                type = it.type,
                annotations = it.annotations.toList().toString(),
                uniqueParameter = parseUniqueParameter(it.type)
            )
        }
    }

    private fun parseUniqueParameter(clazz: Class<*>): ClassView? {
        var aClass: ClassView? = null
        aClass = if (!classParser.typeSeparator.getCollectionTypesClass(clazz)
            && !classParser.typeSeparator.getPrimitiveTypesClass(clazz)
        ){
            classParser.extractClassInfo(clazz)
        } else if (classParser.typeSeparator.getCollectionTypesClass(clazz)){
            null
        } else {
            null
        }
        return aClass
    }
}

data class InputParameter(
    val name: String,
    val type: Class<*>,
    val annotations: String,
    val uniqueParameter: ClassView?
)

data class MethodView(
    val name: String,
    // POST /films
    val path: String,
    val description: String?,
    val summary: String?,
    val parameters: List<InputParameter>?,
    val result: ClassView?
)
