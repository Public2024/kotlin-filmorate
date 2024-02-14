package com.example.javafilmoratekotlin.parsing

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.stereotype.Component
import java.lang.reflect.Field
import java.lang.reflect.MalformedParametersException
import java.lang.reflect.Method
import kotlin.reflect.KParameter
import kotlin.reflect.KType

@Component
class MethodParser(private val classParser: ClassParser) {

    fun extractClassInfo(method: Method): MethodView? {


        return null
    }


}


data class MethodView(
    val name: String,
    // POST /films
    val path: String,
    val description: String?,
    val summary: String?,

//    val parameters: List<ClassView>,
//    val result: ClassView?
)
