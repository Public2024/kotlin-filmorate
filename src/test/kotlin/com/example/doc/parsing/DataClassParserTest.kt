package com.example.doc.parsing


import com.example.doc.model.example.FilmTag
import com.example.doc.model.example.Genre
import io.swagger.v3.oas.annotations.media.Schema
import nonapi.io.github.classgraph.json.Id
import org.example.model.JavaDataModelTest
import org.junit.jupiter.api.Test
import org.springframework.test.util.AssertionErrors
import java.time.LocalDate
import kotlin.reflect.javaType
import kotlin.reflect.typeOf


class DataClassParserTest {

    private val parser = ClassParser()

    @Test
    fun `тест_парсинга_java_класса`() {

        val actualFilm = parser.extractClassInfo(JavaDataModelTest::class.java)

        /*Примитивный тип поля*/
        val expectedIdParse = FieldView(
            name = "id",
            type = "Integer",
            description = "Идентификатор",
            example = "",
            required = false,
            classOfEnum = null,
            classOfComposite = null,
        )

        val actualIdParse = actualFilm!!.fields.find { it.name == "id" }

        AssertionErrors.assertEquals("Pass", expectedIdParse, actualIdParse)
        /*Коллекция с примитвным типом*/
        val expectedTextParse = FieldView(
            name = "text",
            type = "Коллекция<String>",
            description = "Комментарий",
            example = "",
            required = false,
            classOfEnum = null,
            classOfComposite = null,
        )

        val actualTextParse = actualFilm.fields.find { it.name == "text" }

        AssertionErrors.assertEquals("Pass", expectedTextParse, actualTextParse)

        /*Тест поиска schema в конструкторе
        *public Comments(@Schema(description = "Тест жанр") Genre genre) {
        this.genre = genre;
    } */
        val actualUserDescription = actualFilm.fields.find { it.name == "genre" }?.description
        AssertionErrors.assertEquals("Pass", "Тест жанр", actualUserDescription)

        /*Тест парсинг composite type*/
        val expectedGenreParse = FieldView(
            name = "genre",
            type = "Genre",
            description = "Тест жанр",
            example = "",
            required = false,
            classOfEnum = null,
            classOfComposite = null
        )
        val actualUserParse = actualFilm.fields.find { it.name == "genre" }

        AssertionErrors.assertEquals("Pass", expectedGenreParse, actualUserParse)

        val expectedCollectionGenreParse = FieldView(
            name = "genres",
            type = "Коллекция<Genre>",
            description = "Комментарий к жанрам",
            example = "",
            required = false,
            classOfEnum = null,
            classOfComposite = ClassView(
                name = "Genre",
                pkg = "package com.example.javafilmoratekotlin.model",
                description = "Информация о фильме",
                fields = emptyList()
            )
        )

        val actualCollectionGenreParse = actualFilm.fields.find { it.name == "genres" }

        AssertionErrors.assertEquals("Pass", expectedCollectionGenreParse, actualCollectionGenreParse)
    }

    @Test
    fun `тест_парсинга_примитивных_полей_дата_класса`() {

        data class ActualFilm(
            @field:Schema(description = "Идентификатор фильма", required = false)
            var id: Int
        )

        val actualFilmId = parser.extractClassInfo(ActualFilm::class.java)!!.fields.find { it.name == "id" }

        val expectedFilmId = FieldView(
            name = "id",
            type = Int::class.java.toString(),
            description = "Идентификатор фильма",
            example = "",
            required = false,
            classOfEnum = null,
            classOfComposite = null,
        )
        AssertionErrors.assertEquals("Pass", expectedFilmId, actualFilmId)
    }

    @Test
    fun `тест_парсинга_enum_поля_дата_класса`() {
        data class ActualFilm(
            @field:Schema(description = "Тэги", required = true)
            val tags: FilmTag
        )

        val actualFilmEnum = parser.extractClassInfo(ActualFilm::class.java)!!.fields.find { it.name == "tags" }

        val expectedFilmEnum = FieldView(
            name = "tags",
            type = FilmTag::class.java.toString(),
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

        AssertionErrors.assertEquals("Pass", expectedFilmEnum, actualFilmEnum)
    }

    @Test
    fun `тест_парсинга_composite_поля_дата_класса`() {
        data class ActualFilm(
            @field:Schema(description = "Жанр", required = false)
            val genre: Genre
        )

        val actualFilm = parser.extractClassInfo(ActualFilm::class.java)!!.fields.find { it.name == "genre" }

        val expectedFilm =
            FieldView(
                name = "genre",
                type = Genre::class.java.toString(),
                description = "Жанр",
                example = "",
                required = false,
                classOfEnum = null,
                classOfComposite = ClassView(
                    name = "Genre",
                    pkg = "package com.example.javafilmoratekotlin.model",
                    description = "Информация о фильме",
                    fields = emptyList()
                )

            )

        AssertionErrors.assertEquals("Pass", expectedFilm, actualFilm)
    }

    @OptIn(ExperimentalStdlibApi::class)
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

        val actualFilm = parser.extractClassInfo(ActualFilm::class.java)!!.fields.find { it.name == "users" }

        val expectedFilm =
            FieldView(
                name = "users",
                type = typeOf<Collection<ActualUser>>().javaType.toString(),
                description = "Пользователи",
                example = "",
                required = false,
                classOfEnum = null,
                classOfComposite = ClassView(
                    name = "ActualUser",
                    pkg = "package com.example.javafilmoratekotlin.parsing",
                    description = "Информация о пользователе",
                    fields = listOf(
                        FieldView(
                            name = "email",
                            type = String::class.java.toString(),
                            description = "Почта пользователя",
                            example = "",
                            required = false,
                            classOfEnum = null,
                            classOfComposite = null
                        )
                    )
                )
            )

        AssertionErrors.assertEquals("Pass", expectedFilm, actualFilm)
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

        val expectedFilm =
            ClassView(
                    name = "ActualFilm",
                    pkg = "package com.example.javafilmoratekotlin.parsing",
                    description = null,
                    fields = listOf(FieldView(
                        name = "actualFilm",
                        type = ActualFilm::class.java.toString(),
                        description = "Фильм",
                        example = "",
                        required = true,
                        classOfEnum = null,
                        classOfComposite = null
                    )
                )
            )

        val actualFilm = parser.extractClassInfo(ActualFilm::class.java)
        AssertionErrors.assertEquals("Pass", expectedFilm, actualFilm)
    }

    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun `тест_если_поле_класса_коллекция_с_тем_же_типом_что_и_сам_класс_(error_stack_overflow)`(){
        data class ActualFilm(
            @field:Schema(description = "Фильм", required = true)
            val actualFilm: List<ActualFilm>?
        )

       val expectedFilm = ClassView(
            name = "ActualFilm",
            pkg = "package com.example.javafilmoratekotlin.parsing",
            description = null,
            fields = listOf(FieldView(
                name = "actualFilm",
                type = "typeOf<List<ActualFilm>>().javaType.toString()",
                description = "Фильм",
                example = "",
                required = true,
                classOfEnum = null,
                classOfComposite = null
            )
            )
        )

        val actualFilm = parser.extractClassInfo(ActualFilm::class.java)
        AssertionErrors.assertEquals("Pass", expectedFilm, actualFilm)

    }
}