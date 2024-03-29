package com.example.doc

import com.example.doc.model.example.Film
import com.example.doc.model.example.Genre
import com.example.doc.model.example.User
import com.example.doc.parsing.MethodParser
import com.example.doc.service.ApplicationEndpointsFinder
import com.example.doc.service.DocumentationService
import com.example.doc.service.GenerationJsonExamplesEndpoint
import io.swagger.v3.oas.annotations.Operation
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import java.time.LocalDateTime
import java.util.HashMap
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.kotlinFunction

class JsonExampleTest {

    class Test1 {
        @GetMapping("/v")
        @Operation(summary = "")
        fun testMethod(id: Int, film: List<Film>, word: HashMap<User,
                Film>, word2: Map<List<User>, List<Film>>): Map<String, Genre> {
            return emptyMap()
        }
    }

    class Test2 {
        @GetMapping("/v")
        @Operation(summary = "")
        fun testMethod(): LocalDateTime? {
            return null
        }
    }

    @Test
    fun `представление_endpointa_без параметров`(){
        val method = Test2::class.java.declaredMethods.find { it.name == "testMethod" }?.kotlinFunction?.valueParameters
        val actualParameter = GenerationJsonExamplesEndpoint().getJsonOfBody(method!!)
        Assertions.assertEquals ("" ,actualParameter)
    }

    @Test
    fun `представление_json_простого_объекта_параметра_endpoint`(){
        val method = Test1::class.java.declaredMethods.find { it.name == "testMethod" }?.kotlinFunction?.valueParameters
        val parameter = listOf(method!![0])
        val actualParameter = GenerationJsonExamplesEndpoint().getJsonOfBody(parameter)
        Assertions.assertNotNull(actualParameter)
    }

    @Test
    fun `представление_json_Collection_объекта_параметра_endpoint`(){
        val method = Test1::class.java.declaredMethods.find { it.name == "testMethod" }?.kotlinFunction?.valueParameters
        val parameter = listOf(method!![1])
        val actualParameter = GenerationJsonExamplesEndpoint().getJsonOfBody(parameter)
        Assertions.assertNotNull(actualParameter)
    }

    @Test
    fun `представление_json_Map_объекта_параметра_endpoint`(){
        val method = Test1::class.java.declaredMethods.find { it.name == "testMethod" }?.kotlinFunction?.valueParameters
        val parameter = listOf(method!![2])
        val actualParameter = GenerationJsonExamplesEndpoint().getJsonOfBody(parameter)
        Assertions.assertNotNull(actualParameter)
    }

    @Test
    fun `представление_json_Map_объекта_параметра_endpoint_с_дженериками_Collection`(){
        val method = Test1::class.java.declaredMethods.find { it.name == "testMethod" }?.kotlinFunction?.valueParameters
        val parameter = listOf(method!![3])
        val actualParameter = GenerationJsonExamplesEndpoint().getJsonOfBody(parameter)
        Assertions.assertNotNull(actualParameter)
    }

    @Test
    fun `представление_json_всех_параметров_метода`(){
        val method = Test1::class.java.declaredMethods.find { it.name == "testMethod" }?.kotlinFunction?.valueParameters
        val actualParameter = GenerationJsonExamplesEndpoint().getJsonOfBody(method!!)
        Assertions.assertNotNull(actualParameter)
    }

    @Test
    fun `представление_endpointa_без возвращаемого_результата`(){
        class Test3 {
            @GetMapping("/v")
            @Operation(summary = "")
            fun testMethod() {
            }
        }
        val method = Test3::class.java.declaredMethods.find { it.name == "testMethod" }!!.kotlinFunction?.returnType
        val actualResponse = GenerationJsonExamplesEndpoint().getJsonOfResponse(method!!)
        Assertions.assertEquals ("" , actualResponse)
    }

    @Test
    fun `представление_json_проcтого_объекта_response`(){
        class Test4 {
            @GetMapping("/v")
            @Operation(summary = "")
            fun testMethod(): LocalDateTime? {
                return null
            }
        }
        val method = Test4::class.java.declaredMethods.find { it.name == "testMethod" }!!.kotlinFunction?.returnType
        val actualResponse = GenerationJsonExamplesEndpoint().getJsonOfResponse(method!!)
        Assertions.assertNotNull(actualResponse)
    }

    @Test
    fun `представление_json_Collection_объекта_response`(){
        class Test5 {
            @GetMapping("/v")
            @Operation(summary = "")
            fun testMethod(): List<Film> {
                return emptyList()
            }
        }
        val method = Test5::class.java.declaredMethods.find { it.name == "testMethod" }!!.kotlinFunction?.returnType
        val actualResponse = GenerationJsonExamplesEndpoint().getJsonOfResponse(method!!)
        Assertions.assertNotNull(actualResponse)
    }

    @Test
    fun `представление_json_Map_объекта_response`(){
        class Test6 {
            @GetMapping("/v")
            @Operation(summary = "")
            fun testMethod(): Map<User,Film> {
                return emptyMap()
            }
        }
        val method = Test6::class.java.declaredMethods.find { it.name == "testMethod" }!!.kotlinFunction?.returnType
        val actualResponse = GenerationJsonExamplesEndpoint().getJsonOfResponse(method!!)
        Assertions.assertNotNull(actualResponse)
    }

    @Test
    fun `представление_json_Map_объекта_response_c_дженериками_Collection`(){
        class Test7 {
            @GetMapping("/v")
            @Operation(summary = "")
            fun testMethod(): Map<User,List<Film>> {
                return emptyMap()
            }
        }
        val method = Test7::class.java.declaredMethods.find { it.name == "testMethod" }!!.kotlinFunction?.returnType
        val actualResponse = GenerationJsonExamplesEndpoint().getJsonOfResponse(method!!)
        Assertions.assertNotNull(actualResponse)
    }

}