package com.example.javafilmoratekotlin

import com.example.javafilmoratekotlin.controllers.FilmController
import com.example.javafilmoratekotlin.model.Film
import com.example.javafilmoratekotlin.model.User
import com.example.javafilmoratekotlin.parsing.*
import io.swagger.v3.oas.annotations.media.Schema
import org.junit.jupiter.api.Test
import org.springframework.test.util.AssertionErrors
import java.time.DayOfWeek
import java.time.LocalDate
import kotlin.reflect.jvm.javaType
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
        fields = fieldsUser,
    )

    //TODO enum класс genre

    private val genre = ClassView(
        simpleName = "Genre",
        pkg = "com.example.javafilmoratekotlin.model",
        description = "Информация о фильме",
        fields = mutableListOf(),
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
        fields = fieldsFilm,
    )


    //TODO Change Film метод

    private val parameterInputChangeFilmForFilm = InputParameter(
        name = "film",
        type = typeOf<com.example.javafilmoratekotlin.model.Film>(),
        required = true,
        classView = film
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
        classView = null
    )

    private val parameterInputCreateFilmForUsers = InputParameter(
        name = "user",
        type = typeOf<Collection<User>>(),
        required = true,
        classView = user
    )

    private val parameterInputCreateFilmForValues = InputParameter(
        name = "values",
        type = typeOf<List<Int>>(),
        required = true,
        classView = null
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
        fields = mutableListOf(),
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


        val expectedField = expectedClassFilmController.fields
        val actualField = actualClassController.fields

        AssertionErrors.assertEquals("Pass", actualField, expectedField)

    }



    @Test
    fun test_data_class_film() {
        val expectedFilm = film
        val actualFilm = parser.extractClassInfo(Film::class.java)
        AssertionErrors.assertEquals("Pass", expectedFilm, actualFilm)
    }

    @Test
    fun test_data_class_user() {

        @Schema(description = "Информация о пользователе")
        data class TestUser(
            @Schema(description = "Идентификатор пользователя", example = "8")
            val id: Int,
            @Schema(description = "Почта пользователя")
            val favoriteDay: DayOfWeek,
            @Schema(description = "Дата рождения")
            val birthday: LocalDate,
            @Schema(description = "Родители")
            val parents: List<TestUser>,
        )

        val expectedUser = ClassView(
            simpleName = "TestUser",
            pkg = "com.example.javafilmoratekotlin.model",
            description = "Информация о пользователе",
            fields = fieldsUser,
        )
        val actualUser = parser.extractClassInfo(TestUser::class.java)
        AssertionErrors.assertEquals("Pass", expectedUser, actualUser)
    }

}












