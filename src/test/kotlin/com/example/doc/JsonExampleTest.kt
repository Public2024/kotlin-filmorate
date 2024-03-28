package com.example.doc

import com.example.doc.model.example.Film
import com.example.doc.model.example.Genre
import com.example.doc.model.example.User
import com.example.doc.service.GenerationJsonExamplesEndpoint
import io.swagger.v3.oas.annotations.Operation
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.web.bind.annotation.GetMapping
import java.util.HashMap

class JsonExampleTest {

    class Test1 {
        @GetMapping("/v")
        @Operation(summary = "")
        fun testMethod(id: Int, film: List<Film>, word: HashMap<User, Film>, word2: Map<List<User>, List<Film>>): Map<String, Genre> {
            return emptyMap()
        }
    }

    @Test
    fun `представление_json_простого_объекта_параметра_endpoint`(){
        val method = Test1::class.java.declaredMethods.find { it.name == "testMethod" }!!.parameters
        val parameter = listOf(method[0]).toTypedArray()
        val actualParameter = GenerationJsonExamplesEndpoint().getJsonOfBody(parameter)
        Assertions.assertNotNull(actualParameter)
    }

    @Test
    fun `представление_json_Collection_объекта_параметра_endpoint`(){
        val method = Test1::class.java.declaredMethods.find { it.name == "testMethod" }!!.parameters
        val parameter = listOf(method[1]).toTypedArray()
        val actualParameter = GenerationJsonExamplesEndpoint().getJsonOfBody(parameter)
        Assertions.assertNotNull(actualParameter)
    }

    @Test
    fun `представление_json_Map_объекта_параметра_endpoint`(){
        val method = Test1::class.java.declaredMethods.find { it.name == "testMethod" }!!.parameters
        val parameter = listOf(method[2]).toTypedArray()
        val actualParameter = GenerationJsonExamplesEndpoint().getJsonOfBody(parameter)
        println(actualParameter)
        Assertions.assertNotNull(actualParameter)
    }

    @Test
    fun `представление_json_Map_объекта_параметра_endpoint_с_дженериками_Collection`(){
        val method = Test1::class.java.declaredMethods.find { it.name == "testMethod" }!!.parameters
        val parameter = listOf(method[3]).toTypedArray()
        val actualParameter = GenerationJsonExamplesEndpoint().getJsonOfBody(parameter)
        Assertions.assertNotNull(actualParameter)
    }

    @Test
    fun `представление_json_всех_параметров_метода`(){
        val method = Test1::class.java.declaredMethods.find { it.name == "testMethod" }!!.parameters
        val actualParameter = GenerationJsonExamplesEndpoint().getJsonOfBody(method)
        Assertions.assertNotNull(actualParameter)
    }
}