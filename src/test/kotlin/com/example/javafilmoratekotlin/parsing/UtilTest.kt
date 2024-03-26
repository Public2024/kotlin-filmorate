package com.example.javafilmoratekotlin.parsing

import com.example.javafilmoratekotlin.model_example.Genre
import com.example.javafilmoratekotlin.util.TypeSeparator
import io.swagger.v3.oas.annotations.media.Schema
import org.junit.jupiter.api.Test
import org.springframework.test.util.AssertionErrors
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.kotlinFunction

class UtilTest {

    @Test
    fun `тест_редактирования_наименования_типов_полей_классов`() {
        data class Test(
            @Schema(description = "Field1")
            val one: Int,
            @Schema(description = "Filed2")
            val two: Genre,
            @Schema(description = "Field3")
            val three: List<Genre>,
            @Schema(description = "Field4")
            val four: Collection<String>,
            @Schema(description = "Field5")
            val five: Map<String, Genre>,
            @Schema(description = "Field6")
            val six: Map<Int, List<Genre>>,
            @Schema(description = "Field7")
            val seven: Collection<List<String>>
        )

        val expectedNamesField = listOf(
            "int", "Genre", "Коллекция<Genre>", "Коллекция<String>", "Коллекция<String;Genre>",
            "Коллекция<Integer;Коллекция<Genre>>", "Коллекция<Коллекция<String>>"
        )

        val actualNamesFields = Test::class.java.declaredFields.map { TypeSeparator.parseFieldTypeName(it) }

        AssertionErrors.assertEquals("PASS", expectedNamesField, actualNamesFields)

    }

    @Test
    fun `тест_редактирования_наименования_параметров_для_метода_endpoint`() {
        class Test {
            fun testMethod(
                one: Int, two: Genre, three: List<Genre>, four: Collection<String>,
                five: Map<String, Genre>, six: Map<Int, List<Genre>>, seven: Collection<List<String>>
            ) {
            }
        }

        val expectedNamesParameters = Test::class.java.declaredMethods[0].kotlinFunction
            ?.valueParameters?.map { TypeSeparator.parseParameterTypeName(it.type) }

        val actualNamesParameters = listOf("int", "Genre", "Коллекция<Genre>", "Коллекция<String>",
            "Коллекция<String;Genre>",
            "Коллекция<Integer;Коллекция<Genre>>", "Коллекция<Коллекция<String>>"
        )

        AssertionErrors.assertEquals("PASS", expectedNamesParameters, actualNamesParameters)
    }

    @Test
    fun `тест_редактирования_наименования_возвращаемого_результата_метода_endpoint`(){

        class Test {
            fun testMethod() : Collection<List<String>> {
                return emptyList()
            }
        }

        val actualNameResult = TypeSeparator.parseParameterTypeName(Test::class.java.declaredMethods[0].
        kotlinFunction?.returnType!!)

        AssertionErrors.assertEquals("PASS", "Коллекция<Коллекция<String>>", actualNameResult)
    }
}