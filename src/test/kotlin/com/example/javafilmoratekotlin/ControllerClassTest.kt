package com.example.javafilmoratekotlin

import com.example.javafilmoratekotlin.controllers.FilmController
import com.example.javafilmoratekotlin.parsing.ClassParser
import com.example.javafilmoratekotlin.parsing.ClassView
import com.example.javafilmoratekotlin.parsing.InputParameter
import com.example.javafilmoratekotlin.parsing.MethodParser
import com.example.javafilmoratekotlin.util.TypeSeparator
import org.junit.jupiter.api.Test
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType
import kotlin.reflect.full.declaredMemberFunctions


class ControllerClassTest {

    @Test
    fun test() {

        val parser = ClassParser()

        val methodParser = MethodParser(parser)

        val par = FilmController::class.java.declaredMethods.find { it.name.equals("createFilm") }
            ?.let { methodParser.extractMethodInfo(it, parser) }?.parameters

        if (par != null) {
            for(p in par){
                println(p)
            }
        }


    }

}












