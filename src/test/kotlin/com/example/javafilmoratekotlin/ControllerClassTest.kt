package com.example.javafilmoratekotlin

import com.example.javafilmoratekotlin.controllers.FilmController
import org.junit.jupiter.api.Test
import kotlin.reflect.full.declaredMemberFunctions


class ControllerClassTest {

    @Test
    fun test(){
        val methodsFilm = FilmController::class.java.declaredMethods
        val input = methodsFilm.find { it.name.equals("createFilm") }?.parameters?.toList()
        println(input)

    }


}









