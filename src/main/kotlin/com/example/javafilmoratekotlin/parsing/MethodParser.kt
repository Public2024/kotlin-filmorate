package com.example.javafilmoratekotlin.parsing

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KType
import kotlin.reflect.full.declaredMemberFunctions

@Component
class MethodParser(private val classParser: ClassParser) {

    private val requestMapping = mapOf<Class<*>, Boolean>(
        GetMapping::class.java to true,
        PostMapping::class.java to true,
        PutMapping::class.java to true,
        DeleteMapping::class.java to true
    )

    fun extractClassInfo(clazz: Class<*>): List<MethodView>? {
        val items = mutableListOf<KFunction<*>>()
        val methods = clazz.kotlin.declaredMemberFunctions.toList()
        for (m in methods) {
            if (m.annotations.find { it is Operation } != null) {
                items.add(m)
            }
        }
        return items.map { extractMethodInfo(it, classParser) }
    }

    private fun extractMethodInfo(kFunction: KFunction<*>, clazzParser: ClassParser): MethodView {
        val operation = kFunction.annotations.find { it is Operation } as Operation
        return MethodView(
            name = kFunction.name,
            path = "getPathMethod(method)",
            description = operation.description,
            summary = operation.summary,
            parameters = "getAllParameters(kFunction.parameters).filter { it.name!=null }",
            result = kFunction.returnType
        )
    }

    private fun getAllParameters(parameter: List<KParameter>): List<InputParameter> {
        return parameter.map { it ->
            return@map InputParameter(
                name = it.name,
                type = it.type,
                required = (it.annotations.find { it is Parameter } as Parameter).required,
                uniqueParameter = parseUniqueParameter(it.type)
            )
        }
    }

    private fun parseUniqueParameter(type: KType): ClassView? {
        var aClass: ClassView? = null
        aClass = if (!classParser.typeSeparator.isCollection(type.javaClass)
            && !classParser.typeSeparator.getPrimitiveTypesClass(type.javaClass)
        ) {
            null
//            classParser.extractClassInfo(type.javaClass)
        } else if (classParser.typeSeparator.getCollectionTypesClass(type.javaClass)) {
            null
        } else {
            null
        }
        return aClass
    }
}

data class InputParameter(
    val name: String?,
    val type: KType,
    val required: Boolean?,
    val uniqueParameter: ClassView?
)

data class MethodView(
    val name: String,
    // POST /films
    val path: String,
    val description: String?,
    val summary: String?,
    val parameters: String,
    val result: KType
)
