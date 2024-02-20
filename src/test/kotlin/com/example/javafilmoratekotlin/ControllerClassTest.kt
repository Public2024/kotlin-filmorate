package com.example.javafilmoratekotlin

import com.example.javafilmoratekotlin.controllers.FilmController
import com.example.javafilmoratekotlin.parsing.*
import org.junit.jupiter.api.Test


class ControllerClassTest {


    private val parser = ClassParser()

    data class ClassView(
        val simpleName: String,
        val pkg: String,
        val description: String?,
        val fieldNamesSorted: List<String>,
        //TODO: ENUM d обжекстс или отдлельно?
        val methodNameSorted: List<String>,
        val fields: MutableMap<String, FieldView>,
        val methods: List<MethodView>?
    )

    @Test
    fun test1() {
        println(parser.extractClassInfo(FilmController::class.java))
    }

    @Test
    fun test() {

        val methodNameSorted = listOf("changeFilm", "createFilm", "returnAllFilms")

//        val testClassFilmController = ClassView (
//            simpleName = "FilmController",
//            pkg = "com.example.javafilmoratekotlin.controllers",
//            description = null,
//            fieldNamesSorted = emptyList(),
//            methodNameSorted = methodNameSorted.sorted(),
//            fields = emptyList(),
//            methods =
//
//
//
//        )


    }
}












