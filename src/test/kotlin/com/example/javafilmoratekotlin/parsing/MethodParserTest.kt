package com.example.javafilmoratekotlin.parsing

import com.example.javafilmoratekotlin.model.Genre
import io.swagger.v3.oas.annotations.Operation
import org.junit.jupiter.api.Test
import org.springframework.test.util.AssertionErrors
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.ResponseBody
import java.util.*

class MethodParserTest {

    @Test
    fun `тест_метода_который_ничего_не_возвращает `() {
        class Test {
            @PostMapping()
            @Operation(summary = "Метод, который ничего не возвращает")
            fun method() {
            }
        }

        val expected = MethodView(
                name = "method",
                description = "",
                summary = "Метод, который ничего не возвращает",
                responseBody = false,
                parameters = emptyList(),
                result = null
        )
        val test = Test::class.java.methods[0]
        val actual = MethodParser(ClassParser()).extractMethodInfo(test)

        AssertionErrors.assertEquals("Pass", expected, actual)

    }

    @Test
    fun `тест_метода_который_возвращает_примитвный_тип`() {

        class Test {
            @GetMapping()
            @Operation(summary = "Метод, который возвращает примитивный объект")
            fun method(): Int {
                return 1
            }
        }

        val expected = MethodView(
                name = "method",
                description = "",
                summary = "Метод, который возвращает примитивный объект",
                responseBody = false,
                parameters = emptyList(),
                result = OutputResult(
                        type = Int::class.java,
                        uniqueParameter = null
                )
        )

        val test = Test::class.java.methods[0]
        val actual = MethodParser(ClassParser()).extractMethodInfo(test)
        AssertionErrors.assertEquals("Pass", expected, actual)

    }

    @Test
    fun `тест_метода_который_возвращает_сложный_объект`() {
        class Test {
            @GetMapping("/v3")
            @Operation(summary = "Метод, который возвращает сложный объект")
            fun method(): Genre? {
                return null
            }
        }

        val expected = MethodView(
                name = "method",
                description = "",
                summary = "Метод, который возвращает сложный объект",
                responseBody = false,
                parameters = emptyList(),
                result = OutputResult(
                        type = Genre::class.java,
                        uniqueParameter = ClassView(
                                simpleName = "Genre",
                                pkg = "com.example.javafilmoratekotlin.model",
                                description = "Информация о фильме",
                                fields = emptyList()
                        )
                )
        )
        val test = Test::class.java.methods[0]
        val actual = MethodParser(ClassParser()).extractMethodInfo(test)
        AssertionErrors.assertEquals("Pass", expected, actual)
    }

    @Test
    fun `тест_метода_который_возвращает_коллекцию_с_примитивом`() {
        class Test {
            @GetMapping()
            @Operation(summary = "Метод, который возвращает коллекцию с примитивом")
            fun returnCollectionPrimitive(): List<String> {
                return emptyList<String>()
            }
        }

        val expected = MethodView(
                name = "returnCollectionPrimitive",
                description = "",
                summary = "Метод, который возвращает коллекцию с примитивом",
                responseBody = false,
                parameters = emptyList(),
                result = OutputResult(
                        /*??????? java.util.List<java.lang.String> */
                        type = List::class.java,
                        uniqueParameter = null
                )
        )

        val test = Test::class.java.methods[0]
        val actual = MethodParser(ClassParser()).extractMethodInfo(test)
        println(actual)
        /*        AssertionErrors.assertEquals("Pass", expected, actual)*/
    }

    @Test
    fun `тест_метода_который_возвращает_коллекцию_со_сложным_объектом`() {
        class Test {
            @GetMapping("/v5")
            @Operation(summary = "Метод, который возвращает коллекцию со сложным объектом")
            fun returnCollectionComposite(): List<Genre> {
                return emptyList<Genre>()
            }
        }

        val expected = MethodView(
                name = "returnCollectionComposite",
                description = "",
                summary = "Метод, который возвращает коллекцию со сложным объектом",
                responseBody = false,
                parameters = emptyList(),
                result = OutputResult(
                        /*??????? java.util.List<com.example.javafilmoratekotlin.model.Genre> */
                        type = List::class.java,
                        uniqueParameter = ClassView(
                                simpleName = "Genre",
                                pkg = "com.example.javafilmoratekotlin.model",
                                description = "Информация о фильме",
                                fields = emptyList()
                        )
                )
        )

        val test = Test::class.java.methods[0]
        val actual = MethodParser(ClassParser()).extractMethodInfo(test)
        /*        AssertionErrors.assertEquals("Pass", expected, actual)*/
    }

    @Test
    fun `тест_метода_с_примитивным_входящим_параметром`() {
        class Test() {
            @PostMapping()
            @Operation(summary = "Метод с примитивным входящим параметром")
            fun inputPrimitiveParameter(double: Double) {
            }
        }

        val expected = MethodView(
                name = "inputPrimitiveParameter",
                description = "",
                summary = "Метод с примитивным входящим параметром",
                responseBody = false,
                parameters = listOf(
                        InputParameter(
                                name = "double",
                                type = Double::class.java,
                                required = true,
                                classView = null
                        )
                ),
                result = null
        )

        val test = Test::class.java.methods[0]
        val actual = MethodParser(ClassParser()).extractMethodInfo(test)
        AssertionErrors.assertEquals("Pass", expected, actual)
    }

    @Test
    fun `тест_метода_с_композитным_входящим_параметром`(){
        class Test{
            @PostMapping("/v7")
            @Operation(summary = "Метод с композитным входящим параметром")
            fun inputCompositeParameter(genre: Genre){
            }
        }

        val expected = MethodView(
                name = "inputCompositeParameter",
                description = "",
                summary = "Метод с композитным входящим параметром",
                responseBody = false,
                parameters = listOf(
                        InputParameter(
                                name = "genre",
                                type = Genre::class.java,
                                required = true,
                                classView = ClassView(
                                        simpleName = "Genre",
                                        pkg = "com.example.javafilmoratekotlin.model",
                                        description = "Информация о фильме",
                                        fields = emptyList()
                                )
                        )
                ),
                result = null
        )

        val test = Test::class.java.methods[0]
        val actual = MethodParser(ClassParser()).extractMethodInfo(test)
        AssertionErrors.assertEquals("Pass", expected, actual)
    }

    @Test
    fun `тест_метода_с_входящим_параметром_примитивной_коллекцией`(){
        class Test{
            @PostMapping("/v8")
            @Operation(summary = "Метод с параметром примитивной коллекцией")
            fun inputPrimitiveCollectionParameter(list: List<Float>){
            }
        }
        val expected = MethodView(
                name = "inputPrimitiveCollectionParameter",
                description = "",
                summary = "Метод с параметром примитивной коллекцией",
                responseBody = false,
                parameters = listOf(
                        InputParameter(
                                name = "list",
                                /*????? java.util.List<java.lang.Float>*/
                                type = List::class.java,
                                required = true,
                                classView = null
                        )
                ),
                result = null
        )
        val test = Test::class.java.methods[0]
        val actual = MethodParser(ClassParser()).extractMethodInfo(test)

        print(actual)
        /*        AssertionErrors.assertEquals("Pass", expected, actual)*/
    }

    @Test
    fun `тест_метода_с_входящим_параметром_композитной_коллекцией`(){
        class Test{
            @PostMapping("/v9")
            @Operation(summary = "Метод с параметром композитной коллекцией")
            fun inputCompositeCollectionParameter(collection: Collection<Genre>){

            }
        }

        val expected = MethodView(
                name = "inputCompositeCollectionParameter",
                description = "",
                summary = "Метод с параметром композитной коллекцией",
                responseBody = false,
                parameters = listOf(
                        InputParameter(
                                name = "collection",
                                /*????? java.util.Collection<com.example.javafilmoratekotlin.model.Genre>*/
                                type = Collections::class.java,
                                required = true,
                                classView = ClassView(
                                        simpleName = "Genre",
                                        pkg = "com.example.javafilmoratekotlin.model",
                                        description = "Информация о фильме",
                                        fields = emptyList()
                                )
                        )
                ),
                result = null
        )

        val test = Test::class.java.methods[0]
        val actual = MethodParser(ClassParser()).extractMethodInfo(test)
        /*        AssertionErrors.assertEquals("Pass", expected, actual)*/

    }

    @Test
    fun `метод_для_проверки_аннотации_description`(){
        class Test{
            @GetMapping("/v10")
            @Operation(description = "Метод для проверки аннотации с описанием")
            fun methodWithDescription(){
            }
        }

        val test = Test::class.java.methods[0]
        val actual = MethodParser(ClassParser()).extractMethodInfo(test).description
        AssertionErrors.assertEquals("Pass", "Метод для проверки аннотации с описанием", actual)

    }

    @Test
    fun `тест_проверки_аннотации_ResponseBody`(){
        class Test{
            @PutMapping("/v11")
            @Operation(description = "Метод для проверки аннотации ResponseBody")
            @ResponseBody
            fun methodWithResponseBody(){

            }
        }
        val test = Test::class.java.methods[0]
        val actual = MethodParser(ClassParser()).extractMethodInfo(test).responseBody
        AssertionErrors.assertEquals("Pass", true, actual)

    }
}