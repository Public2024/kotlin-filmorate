package com.example.javafilmoratekotlin.parsing

import ch.qos.logback.classic.pattern.Util
import com.example.javafilmoratekotlin.model.Film
import com.example.javafilmoratekotlin.model.FilmTag
import com.example.javafilmoratekotlin.model.Genre
import com.example.javafilmoratekotlin.model.User
import io.swagger.v3.oas.annotations.media.Schema
import nonapi.io.github.classgraph.json.Id
import org.example.model.Comments
import org.hibernate.validator.internal.util.ReflectionHelper
import org.hibernate.validator.internal.util.ReflectionHelper.typeOf
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.test.util.AssertionErrors
import java.lang.reflect.Type
import java.time.LocalDate
import java.util.*
import javax.validation.constraints.Email
import kotlin.reflect.javaType
import kotlin.reflect.jvm.jvmErasure
import kotlin.reflect.typeOf


class DataClassParserTest {

    private val parser = ClassParser()

    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun `тест_парсинга_java_класса`(){

        val actualFilm = parser.extractClassInfo(Comments::class.java)

        /*Примитивный тип поля*/
        val expectedIdParse = FieldView(
             name = "id",
             type = Integer::class.java,
             description = "Идентификатор",
             example = "",
             required = false,
             classOfEnum = null,
             classOfComposite = null,
        )

        val actualIdParse = actualFilm.fields.find { it.name == "id" }

        AssertionErrors.assertEquals("Pass", expectedIdParse, actualIdParse)

        val expectedTextParse = FieldView(
             name = "text",
             type = typeOf<List<String>>().javaType,
             description = "Комментарий",
             example = "",
             required = false,
             classOfEnum = null,
             classOfComposite = null,
        )

        val actualTextParse = actualFilm.fields.find { it.name == "text" }

        AssertionErrors.assertEquals("Pass", expectedTextParse, actualTextParse)

        val classViewMock: ClassView = Mockito.mock(ClassView::class.java)

        val expectedUserParse = FieldView(
             name = "film",
             type = typeOf<Collection<Film>>().javaType,
             description = "Комментарий",
             example = "",
             required = false,
             classOfEnum = null,
             classOfComposite = classViewMock
        )

        val actualFilmParse = actualFilm.fields.find { it.name == "film" }


        println(classViewMock)

    }

    @Test
    fun `тест_парсинга_примитивных_полей_дата_класса_Int_и_String`() {

        data class ActualFilm(
                @field:Schema(description = "Идентификатор фильма", required = false)
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
                                classOfEnum = null,
                                classOfComposite = null,
                        ), FieldView(
                        name = "name",
                        type = String::class.java,
                        description = "Наименование фильма",
                        example = "Пирожок",
                        required = false,
                        classOfEnum = null,
                        classOfComposite = null,
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
                                classOfComposite = null,
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
                        classOfEnum = null,
                        classOfComposite = ClassView(
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
                        classOfEnum = null,
                        classOfComposite = ClassView(
                                simpleName = "ActualUser",
                                pkg = "com.example.javafilmoratekotlin",
                                description = "Информация о пользователе",
                                fields = listOf(FieldView(
                                        name = "email",
                                        type = String::class.java,
                                        description = "Почта пользователя",
                                        example = "",
                                        required = false,
                                        classOfEnum = null,
                                        classOfComposite = null
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