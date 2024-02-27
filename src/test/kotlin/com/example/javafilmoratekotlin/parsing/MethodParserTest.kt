package com.example.javafilmoratekotlin.parsing

import com.example.javafilmoratekotlin.model.User
import org.junit.jupiter.api.Test
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

class MethodParserTest {


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