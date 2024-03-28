package com.example.doc.parsing

import com.example.doc.model.example.Genre
import io.swagger.v3.oas.annotations.Operation
import org.example.controllers.JavaControllerTest
import org.junit.jupiter.api.Test
import org.springframework.test.util.AssertionErrors
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import kotlin.reflect.javaType
import kotlin.reflect.typeOf

class MethodParserJavaTest {

    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun `тест_java_get_возврат_примтивной_коллекции`() {
        val getMethod = JavaControllerTest()::class.java.methods.find { it.name == "returnList" }
        val actualGetMethod = getMethod?.let { MethodParser().extractMethodInfo(it) }
        val expectedGetMethod = MethodView(
            name = "returnList",
            description = "",
            summary = "Возврат коллекции",
            parameters = emptyList(),
            result = OutputResult(
                type = typeOf<List<String>>().javaType.toString(),
                composite = null
            )
        )
        AssertionErrors.assertEquals("Pass", expectedGetMethod, actualGetMethod)
    }

    @Test
    fun `тест_java_post_добавление_композитного_класса`() {
        val postMethod = JavaControllerTest()::class.java.methods.find { it.name == "addCompositeObj" }
        val actualPostMethod = postMethod?.let { MethodParser().extractMethodInfo(it) }
        val expectedPostMethod = MethodView(
            name = "addCompositeObj",
            description = "",
            summary = "Добавление CompositeClass",
            parameters = listOf(
                InputParameter(
                    name = "genre",
                    type = Genre::class.java.toString(),
                    required = true,
                    classView = ClassView(
                        name = "Genre",
                        pkg = "package com.example.javafilmoratekotlin.model",
                        description = "Информация о фильме",
                        fields = emptyList()
                    )
                )
            ),
            result = null
        )
        AssertionErrors.assertEquals("Pass", expectedPostMethod, actualPostMethod)
    }

    @Test
    fun `тест_java_delete_метод_void`() {
        val deleteMethod = JavaControllerTest()::class.java.methods.find { it.name == "deleteById" }
        val actualPostMethod = deleteMethod?.let { MethodParser().extractMethodInfo(it) }
        val expectedDeleteMethod = MethodView(
            name = "deleteById",
            description = "Удаление по id",
            summary = "",
            parameters = listOf(
                InputParameter(
                    name = "id",
                    type = Integer::class.java.toString(),
                    required = false,
                    classView = null
                )
            ),
            result = null
        )
        AssertionErrors.assertEquals("Pass", expectedDeleteMethod, actualPostMethod)
    }

    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun `тест_java_put_метод_параметр_CompositeCollection`() {
        val putMethod = JavaControllerTest()::class.java.methods.find { it.name == "updateCollection" }
        val actualPutMethod = putMethod?.let { MethodParser().extractMethodInfo(it) }
        val expectedPutMethod = MethodView(
            name = "updateCollection",
            description = "Обновление Composite Collection",
            summary = "",
            parameters = listOf(
                InputParameter(
                    name = "collection",
                    type = typeOf<Collection<Genre>>().javaType.toString(),
                    required = false,
                    classView = ClassView(
                        name = "Genre",
                        pkg = "package com.example.javafilmoratekotlin.model",
                        description = "Информация о фильме",
                        fields = emptyList()
                    )
                )
            ),
            result = null
        )
        AssertionErrors.assertEquals("Pass", expectedPutMethod, actualPutMethod)


    }

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
            parameters = emptyList(),
            result = null
        )
        val test = Test::class.java.methods[0]
        val actual = MethodParser().extractMethodInfo(test)

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
            parameters = emptyList(),
            result = OutputResult(
                type = Int::class.java.toString(),
                composite = null
            )
        )

        val test = Test::class.java.methods[0]
        val actual = MethodParser().extractMethodInfo(test)
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
            parameters = emptyList(),
            result = OutputResult(
                type = Genre::class.java.toString(),
                composite = ClassView(
                    name = "Genre",
                    pkg = "package com.example.javafilmoratekotlin.model",
                    description = "Информация о фильме",
                    fields = emptyList()
                )
            )
        )
        val test = Test::class.java.methods[0]
        val actual = MethodParser().extractMethodInfo(test)
        AssertionErrors.assertEquals("Pass", expected, actual)
    }

    @OptIn(ExperimentalStdlibApi::class)
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
            parameters = emptyList(),
            result = OutputResult(
                type = typeOf<List<String>>().javaType.toString(),
                composite = null
            )
        )

        val test = Test::class.java.methods[0]
        val actual = MethodParser().extractMethodInfo(test)

        AssertionErrors.assertEquals("Pass", expected, actual)
    }

    @OptIn(ExperimentalStdlibApi::class)
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
            parameters = emptyList(),
            result = OutputResult(
                type = typeOf<List<Genre>>().javaType.toString(),
                composite = ClassView(
                    name = "Genre",
                    pkg = "com.example.javafilmoratekotlin.model",
                    description = "Информация о фильме",
                    fields = emptyList()
                )
            )
        )

        val test = Test::class.java.methods[0]
        val actual = MethodParser().extractMethodInfo(test)
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
            parameters = listOf(
                InputParameter(
                    name = "double",
                    type = Double::class.java.toString(),
                    required = false,
                    classView = null
                )
            ),
            result = null
        )

        val test = Test::class.java.methods[0]
        val actual = MethodParser().extractMethodInfo(test)
        AssertionErrors.assertEquals("Pass", expected, actual)
    }

    @Test
    fun `тест_метода_с_композитным_входящим_параметром`() {
        class Test {
            @PostMapping("/v7")
            @Operation(summary = "Метод с композитным входящим параметром")
            fun inputCompositeParameter(genre: Genre) {
            }
        }

        val expected = MethodView(
            name = "inputCompositeParameter",
            description = "",
            summary = "Метод с композитным входящим параметром",
            parameters = listOf(
                InputParameter(
                    name = "genre",
                    type = Genre::class.java.toString(),
                    required = false,
                    classView = ClassView(
                        name = "Genre",
                        pkg = "package com.example.javafilmoratekotlin.model",
                        description = "Информация о фильме",
                        fields = emptyList()
                    )
                )
            ),
            result = null
        )

        val test = Test::class.java.methods[0]
        val actual = MethodParser().extractMethodInfo(test)
        AssertionErrors.assertEquals("Pass", expected, actual)
    }

    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun `тест_метода_с_входящим_параметром_примитивной_коллекцией`() {
        class Test {
            @PostMapping("/v8")
            @Operation(summary = "Метод с параметром примитивной коллекцией")
            fun inputPrimitiveCollectionParameter(list: List<Float>) {
            }
        }

        val expected = MethodView(
            name = "inputPrimitiveCollectionParameter",
            description = "",
            summary = "Метод с параметром примитивной коллекцией",
            parameters = listOf(
                InputParameter(
                    name = "list",
                    type = typeOf<List<Float>>().javaType.toString(),
                    required = false,
                    classView = null
                )
            ),
            result = null
        )
        val test = Test::class.java.methods[0]
        val actual = MethodParser().extractMethodInfo(test)

        AssertionErrors.assertEquals("Pass", expected, actual)
    }

    @Test
    fun `тест_метода_с_входящим_параметром_композитной_коллекцией`() {
        class Test {
            @PostMapping("/v9")
            @Operation(summary = "Метод с параметром композитной коллекцией")
            fun inputCompositeCollectionParameter(collection: Collection<Genre>) {
            }
        }

        val expected = MethodView(
            name = "inputCompositeCollectionParameter",
            description = "",
            summary = "Метод с параметром композитной коллекцией",
            parameters = listOf(
                InputParameter(
                    name = "collection",
                    type = "Коллекция<Genre>",
                    required = false,
                    classView = ClassView(
                        name = "Genre",
                        pkg = "package com.example.javafilmoratekotlin.model",
                        description = "Информация о фильме",
                        fields = emptyList()
                    )
                )
            ),
            result = null
        )

        val test = Test::class.java.methods[0]
        val actual = MethodParser().extractMethodInfo(test)
        AssertionErrors.assertEquals("Pass", expected, actual)

    }

    @Test
    fun `метод_для_проверки_аннотации_description`() {
        class Test {
            @GetMapping("/v10")
            @Operation(description = "Метод для проверки аннотации с описанием")
            fun methodWithDescription() {
            }
        }

        val test = Test::class.java.methods[0]
        val actual = MethodParser().extractMethodInfo(test).description
        AssertionErrors.assertEquals("Pass", "Метод для проверки аннотации с описанием", actual)

    }

}