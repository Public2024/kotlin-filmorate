package com.example.javafilmoratekotlin.testControllers

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/test")
@Tag(name = "Имя тестового контроллера", description = "Описание тестового контроллера")
class TestController {

    @PostMapping("/post_test")
    fun postMethod() {
    }

    @GetMapping("/get_test")
    fun getMethod() {
    }

    @PutMapping("/put_test")
    fun putMethod() {
    }

    @DeleteMapping("/delete_test")
    fun deleteMethod() {
    }

    @RequestMapping(method = [RequestMethod.POST], path = ["/request_post"])
    fun requestPost() {
    }

    @RequestMapping(method = [RequestMethod.GET], path = ["/request_get"])
    fun requestGet() {
    }

    @RequestMapping(method = [RequestMethod.DELETE], path = ["/request_delete"])
    fun requestDelete() {
    }

    @RequestMapping(method = [RequestMethod.PUT], path = ["/request_put"])
    fun requestPut() {
    }
}
