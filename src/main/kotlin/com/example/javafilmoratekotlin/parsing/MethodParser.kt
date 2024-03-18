package com.example.javafilmoratekotlin.parsing

import com.example.javafilmoratekotlin.util.TypeSeparator
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import org.springframework.stereotype.Component
import java.lang.reflect.Method
import kotlin.reflect.KParameter
import kotlin.reflect.KType
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.javaType
import kotlin.reflect.jvm.jvmErasure
import kotlin.reflect.jvm.kotlinFunction

@Component
class MethodParser(
     private val classParser: ClassParser
) {

    /*Парсинг метода в MethodView*/
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
        var required = false
        return parameters?.map { it ->
            if (it.annotations.filterIsInstance<Parameter>() != emptyList<Annotation>()) {
                required = (it.annotations.find { it is Parameter } as Parameter).required
            }
            InputParameter(
                 name = it.name,
                 type = it.type.javaType.toString(),
                 required = required,
                 classView = checkOnCompositeParameter(it.type)
            )
        }
    }

    /*Проверка если параметр(или возвращаемый тип) композитный класс*/
    private fun checkOnCompositeParameter(type: KType): ClassView? {

        var aClass =
             if (TypeSeparator.isPrimitive(type.jvmErasure.java)) null
             else classParser.extractClassInfo(type.jvmErasure.java)

        /*Если collection*/
        if (TypeSeparator.isCollection(type.jvmErasure.javaObjectType)) {
            val forCheck = type.arguments.first().type?.jvmErasure?.java
            aClass = if (forCheck?.let { TypeSeparator.isPrimitive(it) } != true) forCheck?.let {
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
             type = returnType.javaType.toString(), uniqueParameter = checkOnCompositeParameter(returnType)
        )
    }
}

data class OutputResult(
     val type: String,
     val uniqueParameter: ClassView?
)

data class InputParameter(
     val name: String?,
     val type: String,
     val required: Boolean,
     val classView: ClassView?
)

data class MethodView(
     val name: String,
     val description: String?,
     val summary: String?,
     val parameters: List<InputParameter>?,
     val result: OutputResult?
)

