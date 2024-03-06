package com.example.javafilmoratekotlin.parsing

import com.example.javafilmoratekotlin.util.TypeSeparator
import io.swagger.v3.oas.annotations.Operation
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import java.lang.reflect.Method
import java.lang.reflect.Type
import kotlin.reflect.KParameter
import kotlin.reflect.KType
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.javaType
import kotlin.reflect.jvm.jvmErasure
import kotlin.reflect.jvm.kotlinFunction

@Component
class MethodParser(private val classParser: ClassParser) {

    val separator = classParser.typeSeparator

    /*Парсинг метода в MethodView*/
    fun extractMethodInfo(method: Method): MethodView {
        var description = ""
        var summary = ""
        var result: OutputResult? = null
        var responseBody = false
        val function = method.kotlinFunction
        val annotation = method.annotations?.toList()

        if (annotation?.filterIsInstance<Operation>()?.isEmpty() != true) {
            val operation = annotation?.filterIsInstance<Operation>()
            description = operation?.first()?.description.toString()
            summary = operation?.first()?.summary.toString()
        }

        if (annotation?.filterIsInstance<ResponseBody>()?.isEmpty() != true) {
            responseBody = true
        }

        if (function?.returnType.toString() != "kotlin.Unit") {
            result = getResult(function?.returnType)
        }
        return MethodView(
                name = method.name,
                description = description,
                summary = summary,
                responseBody = responseBody,
                parameters = getAllParameters(function?.valueParameters),
                result = result
        )
    }

    /*Парсинг входящих параметров метода*/
    fun getAllParameters(parameters: List<KParameter>?):
            List<InputParameter>? {
        var required = true
        return parameters?.map { it ->
            if (it.annotations.filterIsInstance<RequestParam>() != emptyList<Annotation>()) {
                required = (it.annotations.find { it is RequestParam } as RequestParam).required
            }
            InputParameter(
                    name = it.name,
                    type = it.type.javaType,
                    required = required,
                    classView = checkOnCompositeParameter(it.type)
            )
        }
    }

    /*Проверка если параметр(или возвращаемый тип) композитный класс*/
    private fun checkOnCompositeParameter(type: KType): ClassView? {

        var aClass: ClassView? = null
        /*Если primitive или composite*/
        aClass = if (TypeSeparator().isPrimitive(type.jvmErasure.java)) {
            null
        } else ClassParser().extractClassInfo(type.jvmErasure.java)

        /*Если collection*/
        if (type.jvmErasure.javaObjectType.let { TypeSeparator().isCollection(it) }) {
            val forCheck = type.arguments.first().type?.jvmErasure?.java
            aClass = if (forCheck?.let { TypeSeparator().isPrimitive(it) } != true)
                forCheck?.let { ClassParser().extractClassInfo(it) }
            else null
        }
        return aClass
    }

    /*Парсинг возвращаемого типа*/
    private fun getResult(result: KType?): OutputResult {
        return OutputResult(
                type = result?.javaType,
                uniqueParameter = result?.let { checkOnCompositeParameter(it) }
        )
    }
}

    data class OutputResult(
            val type: Type?,
            val uniqueParameter: ClassView?
    )

    data class InputParameter(
            val name: String?,
            val type: Type,
            val required: Boolean,
            val classView: ClassView?
    )

    data class MethodView(
            val name: String,
            val description: String?,
            val summary: String?,
            val responseBody: Boolean?,
            val parameters: List<InputParameter>?,
            val result: OutputResult?
    )

