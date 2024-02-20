package com.example.javafilmoratekotlin.parsing

import io.swagger.v3.oas.annotations.Operation
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.*
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KType
import kotlin.reflect.full.declaredMemberFunctions

@Component
class MethodParser(private val classParser: ClassParser) {

    fun extractClassInfo(clazz: Class<*>): List<MethodView>? {
        val methods = clazz.kotlin.declaredMemberFunctions.toList()
        return filterRequestMappingAnnotation(methods).map { extractMethodInfo(it, classParser) }
    }

    fun filterRequestMappingAnnotation(request: List<KFunction<*>>): List<KFunction<*>> {
        val items = mutableListOf<KFunction<*>>()
        for (r in request) {
            if (r.annotations.filterIsInstance<GetMapping>() != emptyList<Annotation>()) items.add(r)
            if (r.annotations.filterIsInstance<PostMapping>() != emptyList<Annotation>()) items.add(r)
            if (r.annotations.filterIsInstance<PutMapping>() != emptyList<Annotation>()) items.add(r)
            if (r.annotations.filterIsInstance<DeleteMapping>() != emptyList<Annotation>()) items.add(r)
        }
        return items
    }

    private fun getPath(k: KFunction<*>): Array<String> {
        if (k.annotations.filterIsInstance<GetMapping>() != emptyList<Annotation>()) {
            return (k.annotations.find { it is GetMapping } as GetMapping).value
        }
        if (k.annotations.filterIsInstance<GetMapping>() != emptyList<Annotation>()) {
            return (k.annotations.find { it is GetMapping } as GetMapping).value
        }
        if (k.annotations.filterIsInstance<PutMapping>() != emptyList<Annotation>()) {
            return (k.annotations.find { it is PutMapping } as PutMapping).value
        }
        if (k.annotations.filterIsInstance<DeleteMapping>() != emptyList<Annotation>()) {
            return (k.annotations.find { it is DeleteMapping } as DeleteMapping).value
        } else return arrayOf("")
    }

    private fun extractMethodInfo(kFunction: KFunction<*>, clazzParser: ClassParser): MethodView {
        var description = ""
        var summary = ""
        if (kFunction.annotations.filterIsInstance<Operation>() != emptyList<Annotation>()) {
            val operation = kFunction.annotations.find { it is Operation } as Operation
            description = operation.description
            summary = operation.summary
        }
        return MethodView(
            name = kFunction.name,
            path = getPath(kFunction),
            description = description,
            summary = summary,
            parameters = getAllParameters(kFunction.parameters).filter { it.name != null },
            result =  parseUniqueParameter(kFunction.returnType)
        )
    }

    private fun getAllParameters(parameter: List<KParameter>): List<InputParameter> {
        var required = true
        return parameter.map { it ->
            if (it.annotations.filterIsInstance<RequestParam>() != emptyList<Annotation>()) {
                required = (it.annotations.find { it is RequestParam } as RequestParam).required
            }
            return@map InputParameter(
                name = it.name,
                type = it.type,
                required = required,
                uniqueParameter = parseUniqueParameter(it.type)
            )
        }
    }

    private fun parseUniqueParameter(type: KType): ClassView? {
        var aClass: ClassView? = null
        if (classParser.typeSeparator.isPresent(type.javaClass)) {
            aClass = null
        } else if (classParser.typeSeparator.isCollection(type.javaClass)){
            if(!classParser.typeSeparator.isPresent(type.arguments.javaClass))
                aClass = classParser.extractClassInfo(type.arguments.javaClass)
        } else {
            aClass = classParser.extractClassInfo(type.javaClass)
        }
        return aClass
    }

}

data class InputParameter(
    val name: String?,
    val type: KType,
    val required: Boolean,
    val uniqueParameter: ClassView?
)

data class MethodView(
    val name: String,
    // POST /films
    val path: Array<String>,
    val description: String?,
    val summary: String?,
    val parameters: List<InputParameter>,
    val result: ClassView?
)
