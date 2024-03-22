package com.example.javafilmoratekotlin

import com.example.javafilmoratekotlin.model.Film
import com.example.javafilmoratekotlin.model.Genre
import com.example.javafilmoratekotlin.parsing.ClassParser
import com.example.javafilmoratekotlin.parsing.ClassView
import com.example.javafilmoratekotlin.parsing.MethodParser
import com.example.javafilmoratekotlin.service.ApplicationEndpointsFinder
import com.example.javafilmoratekotlin.service.DocumentationService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@SpringBootTest
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

        class GetClassesRelatedToParameter(){

            private val listOfClasses = mutableListOf<ClassView>()

            private fun findClassView(classView: ClassView?){
                classView?.fields?.forEach{ field -> if(field.classOfComposite!=null)
                    listOfClasses.add(field.classOfComposite!!)
                    findClassView(field.classOfComposite)
                }
            }

            fun getAllClasses(classView: ClassView?): List<ClassView>{
                if (classView != null) {
                    listOfClasses.add(classView)
                }
                findClassView(classView)
                return listOfClasses
            }
        }

        GetClassesRelatedToParameter().getAllClasses(test).forEach{ println(it) }

    }

    @Test
    fun `тест_передачи_эндпоинтов_в_документацию`(){
        val endPointFinder = ApplicationEndpointsFinder(MethodParser())
        val generateDoc = DocumentationService(endPointFinder).testBuild().find { it.view?.name == "createFilm"}
        println(generateDoc)
/*        generateDoc.forEach{ println(it) }*/
    }

    @Test
    fun `тест_реализации_получения_всех_ClassView_endpoint`(){
        val actual = ApplicationEndpointsFinder(MethodParser()).findAllEndpoints()
            .find { it.path == "/post_film"}!!.method
        val endPointFinder = ApplicationEndpointsFinder(MethodParser())
        val generateDoc = DocumentationService(endPointFinder).getAllClassesRelatedToEndpoint(actual)

        generateDoc?.forEach { println(it) }
    }

}
