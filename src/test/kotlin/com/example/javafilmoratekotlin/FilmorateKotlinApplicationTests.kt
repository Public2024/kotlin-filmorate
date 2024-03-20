package com.example.javafilmoratekotlin

import com.example.javafilmoratekotlin.model.Film
import com.example.javafilmoratekotlin.model.Genre
import com.example.javafilmoratekotlin.parsing.ClassParser
import com.example.javafilmoratekotlin.parsing.ClassView
import com.example.javafilmoratekotlin.parsing.MethodParser
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import org.junit.jupiter.api.Test
import org.springframework.web.bind.annotation.GetMapping


class FilmorateKotlinApplicationTests {

    @Test
    fun contextLoads() {
    }

    @Test
    fun test() {
        class Test {
            @GetMapping("/v5")
            @Operation(summary = "Метод, который возвращает коллекцию со сложным объектом")
            fun returnCollectionComposite(number: Int, word: String): Map<String, Genre> {
                return emptyMap()
            }
        }

        val method = Test::class.java.declaredMethods[0]

        val test = MethodParser().extractMethodInfo(method)

        println(test)

    }

    @Test
    fun `тест_поиска_вложенных_объектов_в_ClassView`(){
        data class Obj4(
            @field:Schema(description = "number", required = false)
            var number: Int
        )

        data class Obj3(
            @field:Schema(description = "Объект 4", required = false)
            var obj4: Obj4
        )

        data class Obj2(
            @field:Schema(description = "Объект 3", required = false)
            var obj3: Obj3
        )

        data class Obj1(
            @field:Schema(description = "Объект 2", required = false)
            var obj2: Obj2,
            @field:Schema(description = "Объект 3", required = false)
            var obj3: Obj3
        )

        val test = ClassParser().extractClassInfo(Film::class.java)

        val list = mutableListOf<ClassView>()

        test?.let { list.add(it) }

        fun findClassView(classView: ClassView?){
            classView?.fields?.forEach{ field -> if(field.classOfComposite!=null)
                list.add(field.classOfComposite!!)
                findClassView(field.classOfComposite)
            }
        }

        findClassView(test)
        list.forEach { println(it) }



    }

}
