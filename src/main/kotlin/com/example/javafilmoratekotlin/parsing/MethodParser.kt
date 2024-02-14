package com.example.javafilmoratekotlin.parsing

import org.springframework.stereotype.Component
import java.lang.reflect.Method

@Component
class MethodParser(private val classParser: ClassParser) {

    fun extractClassInfo(method: Method): List<MethodView>? {
//        val methods = clazz.declaredMethods.toList()
//        return methods.map{extractMethodInfo(it)}
        return null
    }

//    private fun extractMethodInfo(method: Method): MethodView? {
//
//        return null
//    }


}


data class MethodView(
    val name: String,
    // POST /films
    val path: String,
    val description: String?,
    val summary: String?,
    val parameters: List<ClassView>,
    val result: ClassView?
)
