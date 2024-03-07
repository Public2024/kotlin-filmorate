package com.example.javafilmoratekotlin.parsing

import com.example.javafilmoratekotlin.util.TypeSeparator
import io.swagger.v3.oas.annotations.Operation
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.RequestParam
import java.lang.reflect.Method
import java.lang.reflect.Type
import kotlin.reflect.KParameter
import kotlin.reflect.KType
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.javaType
import kotlin.reflect.jvm.jvmErasure
import kotlin.reflect.jvm.kotlinFunction

@Component
class MethodParser(
    private val classParser: ClassParser,
    private val typeSeparator: TypeSeparator,
) {



    /*Парсинг метода в MethodView*/
    //TODO: с джавовыми методами
    fun extractMethodInfo(method: Method): MethodView {

        val description = extractDescription(method)

        return MethodView(
            name = method.name,
            description = description?.description,
            summary = description?.summary,
            parameters = getAllParameters(method.kotlinFunction?.valueParameters),
            result = getResult(method)
        )
    }

    private fun extractDescription(method: Method): MethodDescription? {
        return method.annotations?.filterIsInstance<Operation>()?.firstOrNull()?.let {
            MethodDescription(it.description, it.summary)
        }
    }

    private data class MethodDescription(
        val description: String,
        val summary: String,
    )

    /*Парсинг входящих параметров метода*/
    fun getAllParameters(
        parameters: List<KParameter>?
    ): List<InputParameter>? {
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

        var aClass =
            if (typeSeparator.isPrimitive(type.jvmErasure.java)) null
            else classParser.extractClassInfo(type.jvmErasure.java)

        /*Если collection*/
        if (typeSeparator.isCollection(type.jvmErasure.javaObjectType)) {
            val forCheck = type.arguments.first().type?.jvmErasure?.java
            aClass = if (forCheck?.let { typeSeparator.isPrimitive(it) } != true) forCheck?.let {
                classParser.extractClassInfo(it)
            }
            else null
        }
        return aClass
    }

    /*Парсинг возвращаемого типа*/
    private fun getResult(method: Method): OutputResult? {
        val returnType = method.kotlinFunction?.returnType ?: return null
        if (returnType.toString() == "kotlin.Unit") return null
        return OutputResult(
            type = returnType.javaType, uniqueParameter = checkOnCompositeParameter(returnType)
        )
    }
}

data class OutputResult(
    val type: Type?, val uniqueParameter: ClassView?
)

data class InputParameter(
    val name: String?, val type: Type, val required: Boolean, val classView: ClassView?
)

data class MethodView(
    val name: String,
    val description: String?,
    val summary: String?,
    val parameters: List<InputParameter>?,
    val result: OutputResult?
)

