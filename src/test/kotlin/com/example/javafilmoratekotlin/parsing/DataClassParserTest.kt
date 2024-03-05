package com.example.javafilmoratekotlin.parsing

import com.example.javafilmoratekotlin.model.FilmTag
import com.example.javafilmoratekotlin.model.Genre
import io.swagger.v3.oas.annotations.media.Schema
import nonapi.io.github.classgraph.json.Id
import org.junit.jupiter.api.Test
import org.springframework.test.util.AssertionErrors
import java.time.LocalDate
import java.util.*
import javax.validation.constraints.Email


class DataClassParserTest {

    private val parser = ClassParser()

    @Test
    fun `тест_парсинга_примитивных_полей_дата_класса_Int_и_String`() {

        data class ActualFilm(
                @Schema(description = "Идентификатор фильма", required = false)
                var id: Int,
                @Schema(description = "Наименование фильма", example = "Пирожок")
                val name: String
        )

        val actualFilm = parser.extractClassInfo(ActualFilm::class.java)

        val expectedFilm = ClassView(
                simpleName = "ActualFilm",
                pkg = "com.example.javafilmoratekotlin",
                description = null,
                fields = listOf(
                        FieldView(
                                name = "id",
                                type = Int::class.java,
                                description = "Идентификатор фильма",
                                example = "",
                                required = false,
                                typeField = TypeField.PRIMITIVE,
                                classOfEnum = null,
                                classOfUnique = null,
                        ), FieldView(
                        name = "name",
                        type = String::class.java,
                        description = "Наименование фильма",
                        example = "Пирожок",
                        required = false,
                        typeField = TypeField.PRIMITIVE,
                        classOfEnum = null,
                        classOfUnique = null,
                )
                )
        )
        AssertionErrors.assertEquals("Pass", expectedFilm, actualFilm)
    }

    @Test
    fun `тест_парсинга_enum_поля_дата_класса`() {
        data class ActualFilm(
                @field:Schema(description = "Тэги", required = true)
                val tags: FilmTag
        )

        val actualFilm = parser.extractClassInfo(ActualFilm::class.java)

        val expectedFilm = ClassView(
                simpleName = "ActualFilm",
                pkg = "com.example.javafilmoratekotlin",
                description = null,
                fields = listOf(
                        FieldView(
                                name = "tags",
                                type = FilmTag::class.java,
                                description = "Тэги",
                                example = "",
                                required = true,
                                typeField = TypeField.ENUM,
                                classOfEnum = listOf(
                                        ClassEnumView(
                                                value = "GORE",
                                                description = "18+"
                                        ), ClassEnumView(
                                        value = "COMEDY",
                                        description = "comedy"
                                ), ClassEnumView(
                                        value = "TRAGEDY",
                                        description = null
                                )
                                ),
                                classOfUnique = null,
                        )
                )
        )

        AssertionErrors.assertEquals("Pass", expectedFilm, actualFilm)
    }

    @Test
    fun `тест_парсинга_composite_поля_дата_класса`() {
        data class ActualFilm(
                @field:Schema(description = "Жанр", required = false)
                val genre: Genre
        )

        val actualFilm = parser.extractClassInfo(ActualFilm::class.java)

        val expectedFilm = ClassView(
                simpleName = "ActualFilm",
                pkg = "com.example.javafilmoratekotlin",
                description = null,
                fields = listOf(FieldView(
                        name = "genre",
                        type = Genre::class.java,
                        description = "Жанр",
                        example = "",
                        required = false,
                        typeField = TypeField.COMPOSITE,
                        classOfEnum = null,
                        classOfUnique = ClassView(
                                simpleName = "Genre",
                                pkg = "com.example.javafilmoratekotlin.model",
                                description = "Информация о фильме",
                                fields = emptyList()
                        )

                ))
        )

        AssertionErrors.assertEquals("Pass", expectedFilm, actualFilm)
    }

    @Test
    fun `тест_парсинга_collection_composite_поля_дата_класса`() {
        @Schema(description = "Информация о пользователе")
        data class ActualUser(
                @Schema(description = "Почта пользователя")
                var email: String,
        )

        data class ActualFilm(
                @field:Schema(description = "Пользователи", required = false)
                val users: Collection<ActualUser>
        )


        val actualFilm = parser.extractClassInfo(ActualFilm::class.java)
        println(actualFilm)

        val actualUser = ActualUser("123")

        val classCollection: Collection<ActualUser> = listOf(actualUser)

        val expectedFilm = ClassView(
                simpleName = "ActualFilm",
                pkg = "com.example.javafilmoratekotlin",
                description = null,
                fields = listOf(FieldView(
                        name = "users",
                        type = Collections::class.java,
                        description = "Пользователи",
                        example = "",
                        required = false,
                        typeField = TypeField.COLLECTION_COMPOSITE,
                        classOfEnum = null,
                        classOfUnique = ClassView(
                                simpleName = "ActualUser",
                                pkg = "com.example.javafilmoratekotlin",
                                description = "Информация о пользователе",
                                fields = listOf(FieldView(
                                        name = "email",
                                        type = String::class.java,
                                        description = "Почта пользователя",
                                        example = "",
                                        required = false,
                                        typeField = TypeField.PRIMITIVE,
                                        classOfEnum = null,
                                        classOfUnique = null
                                ))
                        )
                ))
        )
        println(expectedFilm)
    }

    @Test
    fun `тест_парсинга_аннотации_Schema_c_приставкой_field`() {
        data class ActualFilm(
                @field:Schema(description = "Дата релиза фильма", example = "12-01-94", required = false)
                val releaseDate: LocalDate
        )

        val actualFilmParsing = parser.extractClassInfo(ActualFilm::class.java)

        val actualDescriptionReleaseDate = actualFilmParsing.fields[0].description
        val actualExampleReleaseDate = actualFilmParsing.fields[0].example
        val actualRequiredReleaseDate = actualFilmParsing.fields[0].required

        AssertionErrors.assertEquals("Pass", "Дата релиза фильма", actualDescriptionReleaseDate)
        AssertionErrors.assertEquals("Pass", "12-01-94", actualExampleReleaseDate)
        AssertionErrors.assertEquals("Pass", false, actualRequiredReleaseDate)
    }

    @Test
    fun `тест_парсинга_аннотации_Schema_без_приставки_field`() {
        data class ActualFilm(
                @Schema(description = "Дата релиза фильма", example = "12-01-94", required = false)
                val releaseDate: LocalDate
        )

        val actualFilmParsing = parser.extractClassInfo(ActualFilm::class.java)

        val actualDescriptionReleaseDate = actualFilmParsing.fields[0].description
        val actualExampleReleaseDate = actualFilmParsing.fields[0].example
        val actualRequiredReleaseDate = actualFilmParsing.fields[0].required

        AssertionErrors.assertEquals("Pass", "Дата релиза фильма", actualDescriptionReleaseDate)
        AssertionErrors.assertEquals("Pass", "12-01-94", actualExampleReleaseDate)
        AssertionErrors.assertEquals("Pass", false, actualRequiredReleaseDate)
    }

    @Test
    fun `тест_на_выборку_полей_только_с_аннотацией_Schema`() {
        data class ActualFilm(
                @Id
                var id: Int,
                @Email
                @Schema(description = "Почта пользователя")
                var email: String,
                @field:Schema(description = "Дата релиза фильма", example = "12-01-94", required = false)
                val releaseDate: LocalDate,
                val genre: Genre
        )

        val actualFilmParsing = parser.extractClassInfo(ActualFilm::class.java)
        val actualSizeFields = actualFilmParsing.fields.size

        AssertionErrors.assertEquals("Pass", 2, actualSizeFields)
    }

    @Test
    fun `тест_на_парсинг_аннотации_Schema_над_классом`() {
        @Schema(description = "Фильм")
        data class ActualFilm(
                @Email
                @Schema(description = "Почта пользователя")
                var email: String,
        )

        val actualFilmParsing = parser.extractClassInfo(ActualFilm::class.java).description
        AssertionErrors.assertEquals("Pass", "Фильм", actualFilmParsing)
    }

    enum class FilmTagTest {
        @field:Schema(description = "18+")
        GORE,

        @Schema(description = "comedy")
        COMEDY,
        TRAGEDY
    }

    @Test
    fun `тест_на_парсинг_аннотации_Schema_в_enum_классе`() {
        data class ActualFilm(
                @field:Schema(description = "Тэги", required = true)
                val tags: FilmTagTest
        )

        val actualFilmParsing = parser.extractClassInfo(ActualFilm::class.java)

        val actualParseEnumGoreVal = actualFilmParsing.fields[0].classOfEnum?.get(0)?.value
        val actualParseEnumGoreDesc = actualFilmParsing.fields[0].classOfEnum?.get(0)?.description

        val actualParseEnumComedyVal = actualFilmParsing.fields[0].classOfEnum?.get(1)?.value
        val actualParseEnumComedyDesc = actualFilmParsing.fields[0].classOfEnum?.get(1)?.description

        val actualParseEnumTragedyVal = actualFilmParsing.fields[0].classOfEnum?.get(2)?.value
        val actualParseEnumTragedyDesc = actualFilmParsing.fields[0].classOfEnum?.get(2)?.description

        AssertionErrors.assertEquals("Pass", "GORE", actualParseEnumGoreVal)
        AssertionErrors.assertEquals("Pass", "18+", actualParseEnumGoreDesc)

        AssertionErrors.assertEquals("Pass", "COMEDY", actualParseEnumComedyVal)
        AssertionErrors.assertEquals("Pass", "comedy", actualParseEnumComedyDesc)

        AssertionErrors.assertEquals("Pass", "TRAGEDY", actualParseEnumTragedyVal)
        AssertionErrors.assertEquals("Pass", null, actualParseEnumTragedyDesc)
    }

}