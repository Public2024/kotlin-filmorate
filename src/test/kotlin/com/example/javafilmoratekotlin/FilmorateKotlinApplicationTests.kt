package com.example.javafilmoratekotlin


import com.example.javafilmoratekotlin.model.Genre
import com.example.javafilmoratekotlin.parsing.ClassParser
import io.swagger.v3.oas.annotations.media.Schema
import org.junit.jupiter.api.Test
import java.time.LocalDate

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
			@field:Schema(description = "Жанр", required = false)
			val genre: Genre
		)

		val fruits = listOf<String>("Apple", "Peach", "Banana")

		val parser = ClassParser().extractClassInfo(Film::class.java)
		println(parser)
	}

}
