package com.example.javafilmoratekotlin.parsing

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

    /*Получение метода для парсинга*/
    fun extractMethodInfo(method: Method): MethodView {
        val annotation = method.annotations?.toList()
        val operation = annotation?.filterIsInstance<Operation>()
        val function = method.kotlinFunction
        return MethodView(
                name = method.name,
                description = operation?.first()?.description,
                summary = operation?.first()?.summary,
                responseBody = (annotation?.filterIsInstance<ResponseBody>() != null),
                parameters = getAllParameters(function?.valueParameters),
                result = getResult(function?.returnType)
        )
    }

    /*Парсинг входящих параметров метода*/
    fun getAllParameters(parameters: List<KParameter>?):
            List<InputParameterNew>? {
        var required = true
        return parameters?.map { it ->
            if (it.annotations.filterIsInstance<RequestParam>() != emptyList<Annotation>()) {
                required = (it.annotations.find { it is RequestParam } as RequestParam).required
            }
            InputParameterNew(
                    name = it.name,
                    type = it.type,
                    required = required,
                    classView = checkOnCompositeParameter(it.type)
            )
        }
    }

    /*Проверка если параметр(или возвращаемый тип) композитный класс*/
    fun checkOnCompositeParameter(type: KType): ClassView? {
        var aClass: ClassView? = null
        if (separator.isPresent(type.javaClass))
            aClass = null
        if (type.jvmErasure.javaObjectType.let { separator.isCollection(it) }) {
            val forCheck = type.arguments.first().type?.jvmErasure?.java
            if (forCheck?.let { separator.isPresent(it) } != true)
                aClass = forCheck?.let { classParser.extractClassInfo(it) }
        } else {
            aClass = classParser.extractClassInfo(type.jvmErasure.java)
        }
        return aClass
    }

    /*Парсинг возвращаеиого типа*/
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

data class InputParameterNew(
        val name: String?,
        val type: KType,
        val required: Boolean,
        val classView: ClassView?
)

data class MethodView(
        val name: String,
        val description: String?,
        val summary: String?,
        val responseBody: Boolean,
        val parameters: List<InputParameterNew>?,
        val result: OutputResult
)
