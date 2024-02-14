package com.example.javafilmoratekotlin

import io.swagger.v3.oas.annotations.media.Schema
import org.reflections.Reflections
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.RestController
import kotlin.reflect.full.*


/**
 * ���������� �������� �������
 *
 * @author Artyom Sevostyanov
 */
@SpringBootApplication
class FilmorateKotlinApplication

/**
 * @suppress
 */
fun main(args: Array<String>) {
    runApplication<FilmorateKotlinApplication>(*args)

    val module: String = "com.example"
    val reflections = Reflections(module)

    val dataClasses = reflections.getTypesAnnotatedWith(Schema::class.java)
    val controllerClass = reflections.getTypesAnnotatedWith(RestController::class.java)
//
//    println(ModelParsing().getConstructor(dataClasses))
//    println(ControllerParsing().getMethod(controllerClass))
}








