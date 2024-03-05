package com.example.javafilmoratekotlin.parsing

import com.example.javafilmoratekotlin.FilmorateKotlinApplication
import com.example.javafilmoratekotlin.controllers.FilmController
import com.example.javafilmoratekotlin.model.User
import com.example.javafilmoratekotlin.service.ApplicationEndpointsFinder
import org.junit.jupiter.api.Test
import org.springframework.boot.SpringApplication
import org.springframework.context.ApplicationContext
import org.springframework.web.bind.annotation.*
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.kotlinFunction

class MethodParserTest {

    @Test
    fun `тест получения всех эндпоинтов`() {
        val application: ApplicationContext = SpringApplication.run(FilmorateKotlinApplication::class.java)
        val endpointFinder = ApplicationEndpointsFinder(application)
        /*по key или по value передать часть пути по которому найти методы */
        val correctEndPoints = endpointFinder.findAllEndpoints("com.example")
        for (i in correctEndPoints) {
            println(i)
        }
    }

    private val requestClasses = listOf(
            GetMapping::class.java,
            PostMapping::class.java,
            PutMapping::class.java,
            DeleteMapping::class.java
    )

    @Test
    fun `тест парсинга метода`() {
        val filmController = FilmController::class.java.methods
        val createFilm = filmController.find { it.name.equals("createFilm") }
        val parser = createFilm?.let { MethodParser(ClassParser()).extractMethodInfo(it) }
        println(parser)
    }

    @Test
    fun `тест метода parsing_unique_class`() {

        val filmController = FilmController::class.java.methods

        val createFilm = filmController.find { it.name.equals("createFilm") }?.kotlinFunction?.valueParameters?.get(0)

        val test = filmController.find { it.name.equals("createFilm") }

        val parser = test?.let { MethodParser(ClassParser()).extractMethodInfo(it) }

        println(parser)

    }


    @Test
    fun `тест парсингка параметров метода с дженериком`() {
        class Test {
            @RequestMapping("/v2/data")
            fun getStuff(request: List<User>): User? = null
        }

    }

    @Test
    fun `тест метода который ничего не возвращает `() {
        class Test {
            @RequestMapping(method = [RequestMethod.POST], path = ["/v1/data"])
            fun getStuff(request: Int) {
            }
        }

    }
}