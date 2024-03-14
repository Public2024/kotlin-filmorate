package com.example.javafilmoratekotlin

import com.example.javafilmoratekotlin.parsing.ClassParser
import io.swagger.v3.oas.annotations.media.Schema
import org.junit.jupiter.api.Test
import java.lang.reflect.ParameterizedType


class FilmorateKotlinApplicationTests {

	@Test
	fun contextLoads() {
	}

	@Test
	fun test(){
	}

	@Test
	fun parseFieldOfClass(){
		@Schema(description = "Информация о фильме")
		data class Film(
			//TODO: нужен тест что работает без  field: (надо посикать аннотации не только в полях но и в конструкторах)
			@Schema(description = "Идентификатор фильма", required = false)
			var id: Int,
			@field:Schema(description = "Фильм", required = false)
			val film: List<Film>?
		)

		/*println(Film::class.java)
		println(Film::class.java.declaredFields.find { it.name == "film" }?.annotatedType?.type)
		println(Film::class.java.declaredFields[0].declaringClass)*/

		ClassParser().extractClassInfo(Film::class.java)

		val field = Film::class.java.declaredFields[1]
		val objCollection = (field.genericType as ParameterizedType).actualTypeArguments.first() as Class<*>

		val objCollectionType = (field.genericType as ParameterizedType).actualTypeArguments.first()

	/*	println(objCollection)
		println(Film::class.java)
		println(field.declaringClass)*/
	}

}
