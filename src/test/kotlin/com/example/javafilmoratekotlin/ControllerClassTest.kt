package com.example.javafilmoratekotlin

import com.example.javafilmoratekotlin.controllers.FilmController
import com.example.javafilmoratekotlin.model.Film
import com.example.javafilmoratekotlin.parsing.*
import com.example.javafilmoratekotlin.util.TypeSeparator
import io.swagger.v3.oas.annotations.Operation
import org.junit.jupiter.api.Test
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType
import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.declaredMembers
import kotlin.reflect.full.valueParameters


class ControllerClassTest {

    @Test
    fun test() {
        val parser = ClassParser()

        val items = mutableListOf<KFunction<*>>()

        val methods = FilmController::class.java.kotlin.declaredMemberFunctions.toList()

        for (m in methods) {
            if (m.annotations.find { it is Operation } != null) {
                items.add(m)
            }
        }

        fun extractMethodInfo(kFunction: KFunction<*>, clazzParser: ClassParser) {
            val operation = Operation::class.java
            val name = kFunction.name
            val description = (kFunction.annotations.find { it is Operation } as Operation).description
            val summary = (kFunction.annotations.find { it is Operation } as Operation).summary
            val parameter = kFunction.parameters
            val result = kFunction.returnType
            println(name)
            println(description)
            println(summary)
            println(parameter)
            println(result)
            println("______")
        }


        fun parseUniqueParameter(clazz: Class<*>): ClassView? {
            var aClass: ClassView? = null
            aClass = if (!parser.typeSeparator.getCollectionTypesClass(clazz)
                && !parser.typeSeparator.getPrimitiveTypesClass(clazz)
            ) {
                parser.extractClassInfo(clazz)
            } else if (parser.typeSeparator.getCollectionTypesClass(clazz)) {
                null
            } else {
                null
            }
            return aClass
        }

//
//        for(i in items){
//            extractMethodInfo(i, parser)


        val parameter = items[1].parameters[2].type.arguments
        println(parameter)




    }

}












