package com.example.javafilmoratekotlin

import com.example.javafilmoratekotlin.model.Genre
import com.example.javafilmoratekotlin.parsing.ClassParser
import com.example.javafilmoratekotlin.parsing.MethodParser
import io.swagger.v3.oas.annotations.Operation
import org.junit.jupiter.api.Test
import org.springframework.web.bind.annotation.GetMapping


class FilmorateKotlinApplicationTests {

	@Test
	fun contextLoads() {
	}

	@Test
	fun test(){
		class Test {
			@GetMapping("/v5")
			@Operation(summary = "Метод, который возвращает коллекцию со сложным объектом")
			fun returnCollectionComposite(number: Int, word: String): Map<String, Genre> {
				return emptyMap()
			}
		}

		val method = Test::class.java.declaredMethods[0]

		val test = MethodParser(ClassParser()).extractMethodInfo(method)

		println(test)

	}

	@Test
	fun typeRegularExpression() {
		val collect = "java.util.Collection<com.example.javafilmoratekotlin.model.User>"
		val ob = "com.example.javafilmoratekotlin.model.Film"
		val map = "java.util.Map<java.lang.String, com.example.javafilmoratekotlin.model.Genre>"
		val primitive = "Int"

		val (collection, obj) = collect.split("<")
		val result1 = collection.substringAfterLast(".") + " "+ obj.substringAfterLast(".")


		val result2 = ob.substringAfterLast(".")

		val findCollectionWord = Regex("(Collection)|(List)|(Array)|(ArrayList)|(Set)|(HashMap)|(Map)")
		val result3 = primitive.substringAfterLast(".")


		fun type(word: String): String{
			val collection1 = findCollectionWord.find(collect)?.value ?: ""
			val obj1 = word.substringAfterLast(".")
			var obj2 = ""
			if(word.contains(",")){
				obj2 = word.substringBefore(",").substringAfterLast(".")
			}
			return collection1 + obj2  + obj1
		}

		println(map.substringBefore(",").substringAfterLast("."))
		println(type(collect))
	}


}
