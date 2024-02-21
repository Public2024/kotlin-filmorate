package com.example.javafilmoratekotlin

import com.example.javafilmoratekotlin.controllers.FilmController
import com.example.javafilmoratekotlin.parsing.*
import org.junit.jupiter.api.Test
import org.springframework.test.util.AssertionErrors
import kotlin.reflect.typeOf


class ControllerClassTest {

    private val parser = ClassParser()

    //TODO Change Film метод

    private val parameterInputChangeFilmForFilm = InputParameter(
        name = "film",
        type = typeOf<com.example.javafilmoratekotlin.model.Film>(),
        required = true,
        uniqueParameter = ClassView(
            simpleName = "KTypeImpl",
            pkg = "kotlin.reflect.jvm.internal",
            description = null,
            fieldNamesSorted = emptyList(),
            methodNameSorted = emptyList(),
            fields = mutableMapOf<String, FieldView>(),
            methods = emptyList()
        )
    )

    private val methodViewChangeFilm = MethodView(
        name = "changeFilm",
        path = arrayOf<String>(),
        description = "",
        summary = "Изменить фильм",
        parameters = mutableListOf(parameterInputChangeFilmForFilm),
        result = ClassView(
            simpleName = "KTypeImpl",
            pkg = "kotlin.reflect.jvm.internal",
            description = null,
            fieldNamesSorted = emptyList(),
            methodNameSorted = emptyList(),
            fields = mutableMapOf<String, FieldView>(),
            methods = emptyList()
        )
    )

    //TODO Create Film метод

    private val parameterInputCreateFilmForId = InputParameter(
        name = "id",
        type = typeOf<Int>(),
        required = true,
        uniqueParameter = ClassView(
            simpleName = "KTypeImpl",
            pkg = "kotlin.reflect.jvm.internal",
            description = null,
            fieldNamesSorted = emptyList(),
            methodNameSorted = emptyList(),
            fields = mutableMapOf<String, FieldView>(),
            methods = emptyList()
        )
    )

    private val parameterInputCreateFilmForUsers = InputParameter(
        name = "users",
        type = typeOf<Collection<com.example.javafilmoratekotlin.model.User>>(),
        required = true,
        uniqueParameter = ClassView(
            simpleName = "KTypeImpl",
            pkg = "kotlin.reflect.jvm.internal",
            description = null,
            fieldNamesSorted = emptyList(),
            methodNameSorted = emptyList(),
            fields = mutableMapOf<String, FieldView>(),
            methods = emptyList()
        )
    )

    private val parameterInputCreateFilmForValues = InputParameter(
        name = "values",
        type = typeOf<List<Int>>(),
        required = true,
        uniqueParameter = ClassView(
            simpleName = "KTypeImpl",
            pkg = "kotlin.reflect.jvm.internal",
            description = null,
            fieldNamesSorted = emptyList(),
            methodNameSorted = emptyList(),
            fields = mutableMapOf<String, FieldView>(),
            methods = emptyList()
        )
    )

    private val methodViewCreateFilm = MethodView(
        name = "changeFilm",
        path = arrayOf<String>(),
        description = "",
        summary = "Добавить фильм",
        parameters = mutableListOf(
            parameterInputChangeFilmForFilm,
            parameterInputCreateFilmForId,
            parameterInputCreateFilmForUsers,
            parameterInputCreateFilmForValues
        ),
        result = ClassView(
            simpleName = "KTypeImpl",
            pkg = "kotlin.reflect.jvm.internal",
            description = null,
            fieldNamesSorted = emptyList(),
            methodNameSorted = emptyList(),
            fields = mutableMapOf<String, FieldView>(),
            methods = emptyList()
        )
    )

    //TODO Return All Films метод

    private val methodViewReturnAllFilms = MethodView(
        name = "changeFilm",
        path = arrayOf<String>("/all"),
        description = "",
        summary = "Показать все фильмы",
        parameters = emptyList(),
        result = ClassView(
            simpleName = "KTypeImpl",
            pkg = "kotlin.reflect.jvm.internal",
            description = null,
            fieldNamesSorted = emptyList(),
            methodNameSorted = emptyList(),
            fields = mutableMapOf<String, FieldView>(),
            methods = emptyList()
        )
    )


// TODO: Основной класс

    private val methodsFilmController = mutableListOf(methodViewChangeFilm, methodViewCreateFilm, methodViewReturnAllFilms)

    private val methodNameSortedFilmController = listOf("changeFilm", "createFilm", "returnAllFilms")

    private val fieldsFilmController = mutableMapOf<String, FieldView>()

    private val expectedClassFilmController = ClassView(
        simpleName = "FilmController",
        pkg = "com.example.javafilmoratekotlin.controllers",
        description = null,
        fieldNamesSorted = emptyList(),
        methodNameSorted = methodNameSortedFilmController.sorted(),
        fields = fieldsFilmController,
        methods = methodsFilmController,
    )

    private val actualClassController = parser.extractClassInfo(FilmController::class.java)


    @Test
    fun test1() {
        println(parser.extractClassInfo(FilmController::class.java))
    }

    @Test
    fun test_parameters_class_film_controller() {
        val expectedName = expectedClassFilmController.simpleName
        val actualName = actualClassController.simpleName

        AssertionErrors.assertEquals("Pass", actualName, expectedName)

        val expectedPkg = expectedClassFilmController.pkg
        val actualPkg = actualClassController.pkg

        AssertionErrors.assertEquals("Pass", actualPkg, expectedPkg)

        val expectedDescription = expectedClassFilmController.description
        val actualDescription = actualClassController.description

        AssertionErrors.assertEquals("Pass", actualDescription, expectedDescription)

        val expectedFieldNamesSorted = expectedClassFilmController.fieldNamesSorted
        val actualFieldNamesSorted = actualClassController.fieldNamesSorted

        AssertionErrors.assertEquals("Pass", actualFieldNamesSorted, expectedFieldNamesSorted)

        val expectedMethodNameSorted = expectedClassFilmController.methodNameSorted
        val actualMethodNameSorted = actualClassController.methodNameSorted

        AssertionErrors.assertEquals("Pass", actualMethodNameSorted, expectedMethodNameSorted)

        val expectedField = expectedClassFilmController.fields
        val actualField = actualClassController.fields

        AssertionErrors.assertEquals("Pass", actualField, expectedField)

    }
}












