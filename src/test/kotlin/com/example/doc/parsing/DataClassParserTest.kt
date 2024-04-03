package com.example.doc.parsing


import com.example.doc.model.example.FilmTag
import com.example.doc.model.example.Genre
import io.swagger.v3.oas.annotations.media.Schema
import nonapi.io.github.classgraph.json.Id
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.test.util.AssertionErrors
import java.time.LocalDate


class DataClassParserTest {

    private val parser = ClassParser()

    @Test
    fun `тест_парсинга_примитивных_полей_дата_класса`() {

        data class ActualFilm(
            @field:Schema(description = "Идентификатор фильма", required = false)
            var id: Int
        )

        val actualField = parser.extractClassInfo(ActualFilm::class.java)!!.fields.find { it.name == "id" }

        val expectedField = FieldView(
            name = "id",
            type = "int",
            description = "Идентификатор фильма",
            example = "",
            required = false,
            classOfEnum = null,
            classOfComposite = null,
        )
        Assertions.assertNotNull(expectedField)
        Assertions.assertEquals(expectedField.name, actualField?.name)
        Assertions.assertEquals(expectedField.type, actualField?.type)
        Assertions.assertEquals(expectedField.description, actualField?.description)
        Assertions.assertEquals(expectedField.required, actualField?.required)
        Assertions.assertEquals(expectedField.classOfEnum, actualField?.classOfEnum)
        Assertions.assertEquals(expectedField.classOfComposite, actualField?.classOfComposite)
    }

    @Test
    fun `тест_парсинга_enum_поля_дата_класса`() {
        data class ActualFilm(
            @field:Schema(description = "Тэги", required = true)
            val tags: FilmTag
        )

        val actualField = parser.extractClassInfo(ActualFilm::class.java)!!.fields.find { it.name == "tags" }

        val expectedField = FieldView(
            name = "tags",
            type = "FilmTag",
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

        Assertions.assertNotNull(expectedField)
        Assertions.assertEquals(expectedField.name, actualField?.name)
        Assertions.assertEquals(expectedField.type, actualField?.type)
        Assertions.assertEquals(expectedField.description, actualField?.description)
        Assertions.assertEquals(expectedField.required, actualField?.required)
        Assertions.assertEquals(expectedField.classOfEnum, actualField?.classOfEnum)
        Assertions.assertEquals(expectedField.classOfComposite, actualField?.classOfComposite)
    }

    @Test
    fun `тест_парсинга_composite_поля_дата_класса`() {
        data class ActualFilm(
            @field:Schema(description = "Жанр", required = false)
            val genre: Genre
        )

        val actualField = parser.extractClassInfo(ActualFilm::class.java)!!.fields.find { it.name == "genre" }

        val expectedField =
            FieldView(
                name = "genre",
                type = "Genre",
                description = "Жанр",
                example = "",
                required = false,
                classOfEnum = null,
                classOfComposite = ClassView(
                    name = "Genre",
                    pkg = "package com.example.doc.model.example",
                    description = "Информация о фильме",
                    fields = emptyList()
                )

            )

        Assertions.assertNotNull(expectedField)
        Assertions.assertEquals(expectedField.name, actualField?.name)
        Assertions.assertEquals(expectedField.type, actualField?.type)
        Assertions.assertEquals(expectedField.description, actualField?.description)
        Assertions.assertEquals(expectedField.required, actualField?.required)
        Assertions.assertEquals(expectedField.classOfEnum, actualField?.classOfEnum)
        Assertions.assertEquals(expectedField.classOfComposite, actualField?.classOfComposite)
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

        val actualField = parser.extractClassInfo(ActualFilm::class.java)!!.fields.find { it.name == "users" }

        val expectedField =
            FieldView(
                name = "users",
                type = ActualUser::class.java.typeName,
                description = "Пользователи",
                example = "",
                required = false,
                classOfEnum = null,
                classOfComposite = null
            )

        Assertions.assertNotNull(expectedField)
        Assertions.assertEquals(expectedField.name, actualField?.name)
        Assertions.assertEquals(expectedField.description, actualField?.description)
        Assertions.assertEquals(expectedField.required, actualField?.required)
        Assertions.assertEquals(expectedField.classOfEnum, actualField?.classOfEnum)
    }

    @Test
    fun `тест_парсинга_composite_поля_в_классe`(){
        @Schema(description = "Информация о пользователе")
        data class ActualUser(
            @Schema(description = "Почта пользователя")
            var email: String,
        )

        data class ActualFilm(
            @field:Schema(description = "Пользователи", required = false)
            val users: Collection<ActualUser>
        )

        val actualField = parser.extractClassInfo(ActualFilm::class.java)!!.fields
            .find { it.name == "users" }?.classOfComposite

        val expectedField = ClassView(
            name = "ActualUser",
            pkg = "package com.example.doc.parsing",
            description = "Информация о пользователе",
            fields = listOf(
                FieldView(
                    name = "email",
                    type = "String",
                    description = "Почта пользователя",
                    example = "",
                    required = false,
                    classOfEnum = null,
                    classOfComposite = null
                )
            )
        )

        Assertions.assertNotNull(actualField)
        Assertions.assertEquals(expectedField.name, actualField?.name)
        Assertions.assertEquals(expectedField.description, actualField?.description)
        Assertions.assertEquals(expectedField.pkg, actualField?.pkg)
        Assertions.assertNotNull(actualField?.fields)
        Assertions.assertEquals(expectedField.fields.first().name, actualField?.fields?.first()?.name)
        Assertions.assertEquals(expectedField.fields.first().type, actualField?.fields?.first()?.type)
        Assertions.assertEquals(expectedField.fields.first().description, actualField?.fields?.first()?.description)
        Assertions.assertEquals(expectedField.fields.first().required, actualField?.fields?.first()?.required)
        Assertions.assertEquals(expectedField.fields.first().classOfEnum, actualField?.fields?.first()?.classOfEnum)
        Assertions.assertEquals(expectedField.fields.first().classOfComposite, actualField?.fields?.first()?.classOfComposite)
    }



    @Test
    fun `тест_парсинга_аннотации_Schema_c_приставкой_field`() {
        data class ActualFilm(
            @field:Schema(description = "Дата релиза фильма", example = "12-01-94", required = false)
            val releaseDate: LocalDate
        )

        val actualFilmParsing = parser.extractClassInfo(ActualFilm::class.java)

        val actualDescriptionReleaseDate = actualFilmParsing!!.fields[0].description
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

        val actualDescriptionReleaseDate = actualFilmParsing!!.fields[0].description
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
            @Schema(description = "Почта пользователя")
            var email: String,
            @field:Schema(description = "Дата релиза фильма", example = "12-01-94", required = false)
            val releaseDate: LocalDate,
            val genre: Genre
        )

        val actualFilmParsing = parser.extractClassInfo(ActualFilm::class.java)
        val actualSizeFields = actualFilmParsing!!.fields.size

        AssertionErrors.assertEquals("Pass", 2, actualSizeFields)
    }

    @Test
    fun `тест_на_парсинг_аннотации_Schema_над_классом`() {
        @Schema(description = "Фильм")
        data class ActualFilm(
            @Schema(description = "Почта пользователя")
            var email: String,
        )

        val actualFilmParsing = parser.extractClassInfo(ActualFilm::class.java)!!.description
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

        val actualParseEnumGoreVal = actualFilmParsing!!.fields[0].classOfEnum?.get(0)?.value
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

    @Test
    fun `тест_если_поле_класса_тот_же_тип_что_и_сам_класс_(error_stack_overflow)`(){
        data class ActualFilm(
            @field:Schema(description = "Фильм", required = true)
            val actualFilm: ActualFilm?
        )

        val actualFilm = parser.extractClassInfo(ActualFilm::class.java)

        Assertions.assertNotNull(actualFilm)
        Assertions.assertEquals("ActualFilm", actualFilm?.name)
        Assertions.assertEquals("package com.example.doc.parsing", actualFilm?.pkg)
        Assertions.assertEquals("actualFilm", actualFilm?.fields?.first()?.name)
        Assertions.assertEquals("ActualFilm", actualFilm?.fields?.first()?.type)
        Assertions.assertEquals("Фильм", actualFilm?.fields?.first()?.description)
        Assertions.assertEquals(true, actualFilm?.fields?.first()?.required)
        Assertions.assertEquals(null, actualFilm?.fields?.first()?.classOfEnum)
        Assertions.assertEquals(null, actualFilm?.fields?.first()?.classOfComposite)

    }

    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun `тест_если_поле_класса_коллекция_с_тем_же_типом_что_и_сам_класс_(error_stack_overflow)`(){
        data class ActualFilm(
            @field:Schema(description = "Фильм", required = true)
            val actualFilm: List<ActualFilm>?
        )

        val actualFilm = parser.extractClassInfo(ActualFilm::class.java)

        Assertions.assertNotNull(actualFilm)
        Assertions.assertEquals("ActualFilm", actualFilm?.name)
        Assertions.assertEquals("package com.example.doc.parsing", actualFilm?.pkg)
        Assertions.assertEquals("actualFilm", actualFilm?.fields?.first()?.name)
        Assertions.assertEquals("Фильм", actualFilm?.fields?.first()?.description)
        Assertions.assertEquals(true, actualFilm?.fields?.first()?.required)
        Assertions.assertEquals(null, actualFilm?.fields?.first()?.classOfEnum)
        Assertions.assertEquals(null, actualFilm?.fields?.first()?.classOfComposite)
    }
}