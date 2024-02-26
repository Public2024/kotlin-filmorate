package com.example.javafilmoratekotlin

import com.example.javafilmoratekotlin.controllers.FilmController
import com.example.javafilmoratekotlin.model.Film
import com.example.javafilmoratekotlin.model.User
import com.example.javafilmoratekotlin.parsing.*
import org.junit.jupiter.api.Test
import org.springframework.test.util.AssertionErrors
import java.lang.reflect.Type
import kotlin.reflect.KType
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.jvm.javaType
import kotlin.reflect.jvm.jvmErasure
import kotlin.reflect.typeOf


class ControllerClassTest {

    private val parser = ClassParser()

    //TODO класс User 

    private val fieldIdUser = FieldView(
        name = "id",
        type = "int",
        description = "Идентификатор пользователя",
        example = "",
        required = false,
        typeField = TypeField.PRIMITIVE,
        classOfEnum = null,
        classOfUnique = null,
        classOfPrimitiveCollection = null,
        classOfUniqueCollection = null,

        )

    private val fieldEmailUser = FieldView(
        name = "email",
        type = "String",
        description = "Почта пользователя",
        example = "",
        required = false,
        typeField = TypeField.PRIMITIVE,
        classOfEnum = null,
        classOfUnique = null,
        classOfPrimitiveCollection = null,
        classOfUniqueCollection = null
    )

    private val fieldLoginUser = FieldView(
        name = "login",
        type = "String",
        description = "Логин пользователя",
        example = "",
        required = false,
        typeField = TypeField.PRIMITIVE,
        classOfEnum = null,
        classOfUnique = null,
        classOfPrimitiveCollection = null,
        classOfUniqueCollection = null
    )

    private val fieldNameUser = FieldView(
        name = "name",
        type = "String",
        description = "Имя пользователя",
        example = "",
        required = false,
        typeField = TypeField.PRIMITIVE,
        classOfEnum = null,
        classOfUnique = null,
        classOfPrimitiveCollection = null,
        classOfUniqueCollection = null
    )

    private val fieldBirthdayUser = FieldView(
        name = "birthday",
        type = "LocalDate",
        description = "Дата рождения",
        example = "",
        required = false,
        typeField = TypeField.PRIMITIVE,
        classOfEnum = null,
        classOfUnique = null,
        classOfPrimitiveCollection = null,
        classOfUniqueCollection = null
    )

    private val fieldsUser = mutableListOf(
        fieldIdUser, fieldEmailUser,
        fieldLoginUser, fieldNameUser,
        fieldBirthdayUser
    )

    private val user = ClassView(
        simpleName = "User",
        pkg = "com.example.javafilmoratekotlin.model",
        description = "Информация о пользователе",
        fieldNamesSorted = listOf("birthday", "email", "id", "login", "name"),
        methodNameSorted = emptyList(),
        fields = fieldsUser,
        methods = emptyList()
    )

    //TODO enum класс genre

    private val genre = ClassView(
        simpleName = "Genre",
        pkg = "com.example.javafilmoratekotlin.model",
        description = "Информация о фильме",
        fieldNamesSorted = emptyList(),
        methodNameSorted = emptyList(),
        fields = mutableListOf(),
        methods = emptyList()
    )

    //TODO film class

    private val fieldIdFilm = FieldView(
        name = "id",
        type = "int",
        description = "Идентификатор фильма",
        example = "",
        required = false,
        typeField = TypeField.PRIMITIVE,
        classOfEnum = null,
        classOfUnique = null,
        classOfPrimitiveCollection = null,
        classOfUniqueCollection = null
    )

    private val fieldNameFilm = FieldView(
        name = "name",
        type = "String",
        description = "Наименование фильма",
        example = "Пирожок",
        required = false,
        typeField = TypeField.PRIMITIVE,
        classOfEnum = null,
        classOfUnique = null,
        classOfPrimitiveCollection = null,
        classOfUniqueCollection = null
    )

    private val fieldReleaseDateFilm = FieldView(
        name = "releaseDate",
        type = "LocalDate",
        description = "Дата релиза фильма",
        example = "",
        required = false,
        typeField = TypeField.PRIMITIVE,
        classOfEnum = null,
        classOfUnique = null,
        classOfPrimitiveCollection = null,
        classOfUniqueCollection = null
    )

    private val fieldDurationFilm = FieldView(
        name = "duration",
        type = "Integer",
        description = "Продолжительность фильма",
        example = "",
        required = false,
        typeField = TypeField.PRIMITIVE,
        classOfEnum = null,
        classOfUnique = null,
        classOfPrimitiveCollection = null,
        classOfUniqueCollection = null
    )

    private val fieldTagsFilm = FieldView(
        name = "tags",
        type = "FilmTag",
        description = "Тэги",
        example = "",
        required = true,
        typeField = TypeField.ENUM,
        classOfEnum = listOf(
            ClassEnumView(value = "GORE", description = "18+"),
            ClassEnumView(value = "COMEDY", description = "comedy"),
            ClassEnumView(value = "TRAGEDY", description = null)
        ),
        classOfUnique = null,
        classOfPrimitiveCollection = null,
        classOfUniqueCollection = null
    )


    private val fieldViewGenre = FieldView(
        name = "genre",
        type = "Genre",
        description = "Жанр",
        example = "",
        required = false,
        typeField = TypeField.UNIQUE,
        classOfEnum = null,
        classOfUnique = genre,
        classOfPrimitiveCollection = null,
        classOfUniqueCollection = null
    )


    private val fieldViewUsers = FieldView(
        name = "users",
        type = "Collection",
        description = "Пользователи",
        example = "",
        required = false,
        typeField = TypeField.COLLECTION_UNIQUE,
        classOfUnique = null,
        classOfEnum = null,
        classOfPrimitiveCollection = null,
        classOfUniqueCollection = user
    )


    private val fieldsFilm = mutableListOf(
        fieldIdFilm, fieldNameFilm,
        fieldReleaseDateFilm,
        fieldDurationFilm, fieldTagsFilm, fieldViewGenre,
        fieldViewUsers
    )

    val comparator = Comparator<FieldView> { p1, p2 ->
        p1.name.compareTo(p2.name)
    }


    private val film = ClassView(
        simpleName = "Film",
        pkg = "com.example.javafilmoratekotlin.model",
        description = "Информация о фильме",
        fieldNamesSorted = listOf("duration", "genre", "id", "name", "releaseDate", "tags", "users"),
        methodNameSorted = emptyList(),
        fields = fieldsFilm,
        methods = emptyList()
    )


    //TODO Change Film метод

    private val parameterInputChangeFilmForFilm = InputParameter(
        name = "film",
        type = typeOf<com.example.javafilmoratekotlin.model.Film>(),
        required = true,
        uniqueParameter = film
    )

    private val methodViewChangeFilm = MethodView(
        name = "changeFilm",
        path = arrayOf<String>(),
        description = "",
        summary = "Изменить фильм",
        parameters = mutableListOf(parameterInputChangeFilmForFilm),
        result = OutputResult(
            type = null,
            uniqueParameter = film
        )
    )

    //TODO Create Film метод

    private val parameterInputCreateFilmForId = InputParameter(
        name = "id",
        type = typeOf<Int>(),
        required = true,
        uniqueParameter = null
    )

    private val parameterInputCreateFilmForUsers = InputParameter(
        name = "user",
        type = typeOf<Collection<com.example.javafilmoratekotlin.model.User>>(),
        required = true,
        uniqueParameter = user
    )

    private val parameterInputCreateFilmForValues = InputParameter(
        name = "values",
        type = typeOf<List<Int>>(),
        required = true,
        uniqueParameter = null
    )

    private val methodViewCreateFilm = MethodView(
        name = "createFilm",
        path = arrayOf<String>(),
        description = "",
        summary = "Добавить фильм",
        parameters = mutableListOf(
            parameterInputChangeFilmForFilm,
            parameterInputCreateFilmForId,
            parameterInputCreateFilmForUsers,
            parameterInputCreateFilmForValues
        ),
        result = OutputResult(
            type = typeOf<Film>().javaType,
            uniqueParameter = film
        )
    )

    //TODO Return All Films метод

    private val methodViewReturnAllFilms = MethodView(
        name = "returnAllFilms",
        path = arrayOf<String>("/all"),
        description = "",
        summary = "Показать все фильмы",
        parameters = emptyList(),
        result = OutputResult(
            type = typeOf<ArrayList<Film>>().javaType,
            uniqueParameter = film
        )
    )


// TODO: Основной класс FilmController

    private val methodsFilmController =
        mutableListOf(methodViewChangeFilm, methodViewCreateFilm, methodViewReturnAllFilms)

    private val methodNameSortedFilmController = listOf("changeFilm", "createFilm", "returnAllFilms")


    private val expectedClassFilmController = ClassView(
        simpleName = "FilmController",
        pkg = "com.example.javafilmoratekotlin.controllers",
        description = null,
        fieldNamesSorted = emptyList(),
        methodNameSorted = methodNameSortedFilmController.sorted(),
        fields = mutableListOf<FieldView>(),
        methods = methodsFilmController,
    )

    private val actualClassController = parser.extractClassInfo(FilmController::class.java)

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

    @Test
    fun test_method_change_film() {
        val expectedChangeFilm = expectedClassFilmController.methods?.find { it.name == ("changeFilm") }
        val actualChangeFilm = actualClassController.methods?.find { it.name == ("changeFilm") }

        AssertionErrors.assertEquals("Pass", expectedChangeFilm.toString(), actualChangeFilm.toString())
    }

    @Test
    fun test_method_return_all_films() {
        val expectedReturnAllFilms = expectedClassFilmController.methods?.find { it.name == ("returnAllFilms") }
        val actualReturnAllFilms = actualClassController.methods?.find { it.name == ("returnAllFilms") }

        AssertionErrors.assertEquals("Pass", expectedReturnAllFilms.toString(), actualReturnAllFilms.toString())

    }

    @Test
    fun test_method_create_film() {
        val expectedCreateFilm = expectedClassFilmController.methods?.find { it.name == ("createFilm") }?.result
        val actualCreateFilm = actualClassController.methods?.find { it.name == ("createFilm") }?.result

        AssertionErrors.assertEquals("Pass", expectedCreateFilm.toString(), actualCreateFilm.toString())

    }

    @Test
    fun test_data_class_film() {
        val expectedFilm = film
        val actualFilm = parser.extractClassInfo(Film::class.java)
        AssertionErrors.assertEquals("Pass", expectedFilm, actualFilm)
    }

    @Test
    fun test_data_class_user() {
        val expectedUser = user
        val actualUser = parser.extractClassInfo(User::class.java)
        AssertionErrors.assertEquals("Pass", expectedUser, actualUser)
    }

}












