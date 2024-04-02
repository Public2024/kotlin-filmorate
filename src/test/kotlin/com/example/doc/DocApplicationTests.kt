package com.example.doc

import com.example.doc.model.example.Film
import com.example.doc.model.example.Genre
import com.example.doc.model.example.User
import com.example.doc.parsing.ClassParser
import com.example.doc.parsing.ClassView
import com.example.doc.parsing.FieldWithAnnotation
import com.example.doc.parsing.MethodParser
import com.example.doc.service.ApplicationEndpointsFinder
import com.example.doc.service.DocumentationService
import com.example.doc.service.GenerationJsonExamplesEndpoint
import com.example.doc.util.EasyRandomUtil
import com.example.doc.util.SerializationUtil.globalJsonMapper
import com.fasterxml.jackson.core.type.TypeReference
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import net.bytebuddy.implementation.bytecode.assign.InstanceCheck
import org.jeasy.random.EasyRandom
import org.jeasy.random.randomizers.collection.MapRandomizer
import org.jeasy.random.*
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import java.lang.reflect.Field
import java.lang.reflect.Parameter
import java.lang.reflect.ParameterizedType
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import kotlin.reflect.KParameter
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.jvmErasure
import kotlin.reflect.jvm.kotlinFunction


@SpringBootTest
class DocApplicationTests {

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

        val test = MethodParser(GenerationJsonExamplesEndpoint()).extractMethodInfo(method)

        println(test)

    }

    @Test
    fun `тест_поиска_вложенных_объектов_в_ClassView`() {
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

        class GetClassesRelatedToParameter() {

            private val listOfClasses = mutableListOf<ClassView>()

            private fun findClassView(classView: ClassView?) {
                classView?.fields?.forEach { field ->
                    if (field.classOfComposite != null)
                        listOfClasses.add(field.classOfComposite!!)
                    findClassView(field.classOfComposite)
                }
            }

            fun getAllClasses(classView: ClassView?): List<ClassView> {
                if (classView != null) {
                    listOfClasses.add(classView)
                }
                findClassView(classView)
                return listOfClasses
            }
        }

        GetClassesRelatedToParameter().getAllClasses(test).forEach { println(it) }

    }

    @Test
    fun `тест_генерации_объекта_easyrandom`() {

        data class ObjWithPrimitiveFields(
            val one: Int,
            val two: Boolean,
            val three: Double,
            val four: Float,
            val five: Integer,
            val six: Long,
            val seven: BigDecimal,
            val eight: String,
            val nine: LocalDateTime,
            val ten: LocalDate,
            val eleven: Instant,
            val twelve: UUID,
            val thirteen: Unit
        )

        fun getExample(clazz: Class<*>): Any? {
            /*Попробовать класс EasyRandom из либы*/
            val exampleObject = EasyRandomUtil.easyRandom.objects(clazz, 1).toList().first()
            return exampleObject
        }

        println(getExample(Film::class.java))

    }

    @Test
    fun `тест_класса_с_map`() {
        data class ObjWithMaps(
            @Schema(description = "field1")
            val one: Map<String, Film>,
            @Schema(description = "field2")
            val two: HashMap<String, User>,
            @Schema(description = "field3")
            val three: Map<Int, String>
        )

        val parse = ClassParser().extractClassInfo(ObjWithMaps::class.java)

        val collectionMaps = listOf<Class<*>>(
            Map::class.javaObjectType,
            java.util.HashMap::class.javaObjectType,
            Map::class.java,
            java.util.HashMap::class.java
        )

        fun getObjCollection(field: Field): Class<*> {
            return (field.genericType as ParameterizedType).actualTypeArguments[1] as Class<*>
        }

        println(getObjCollection(ObjWithMaps::class.java.declaredFields[1]))
    }

    @Test
    fun `сериализация_в_Json_параметров_endpoint`() {

        class Test {
            @GetMapping("/v5")
            @Operation(summary = "Метод, который возвращает коллекцию со сложным объектом")
            fun returnCollectionComposite(film: Film, word: HashMap<User, Film>): Map<String, Genre> {
                return emptyMap()
            }
        }

        val test = Test::class.java.declaredMethods[0].parameters

        fun createExampleClass(clazz: Class<*>): Any {
            val exampleObject = EasyRandomUtil.easyRandom.objects(clazz, 1).toList().first()
            return exampleObject
        }

        fun <T> fromJson(json: String, typeReference: TypeReference<T>): T {
            return globalJsonMapper.readValue(json, typeReference)
        }


        fun createObj(parameter: Parameter): Class<*> {
            val clazz = parameter.type
            return clazz
        }

        println(createExampleClass(createObj(test[0])))

    }

    @Test
    fun `тест_создания_примера_объекта_Map`() {
        data class ObjWithMaps(
            @Schema(description = "field1")
            val one: Map<String, Film>,
            @Schema(description = "field2")
            val two: HashMap<String, User>,
            @Schema(description = "field3")
            val three: Map<Int, String>
        )

        val collectionMaps = listOf<Class<*>>(
            Map::class.javaObjectType,
            HashMap::class.javaObjectType,
            Map::class.java,
            HashMap::class.java
        )

        val collections = listOf<Class<*>>(
            Collection::class.javaObjectType,
            List::class.javaObjectType,
            Array::class.javaObjectType,
            Set::class.javaObjectType,
            ArrayList::class.java,


            Collection::class.java,
            List::class.java,
            Array::class.java,
            ArrayList::class.java,
            Set::class.java,
        )

        class Test {
            @GetMapping("/v5")
            @Operation(summary = "Метод, который возвращает коллекцию со сложным объектом")
            fun returnCollectionComposite(film: Film, word: HashMap<User, Film>) {
            }
        }

        fun createExampleObjFromMap(parameter: Parameter): Any {
            val obj = mutableMapOf<Any, Any>()
            val key = (parameter.parameterizedType as ParameterizedType).actualTypeArguments[0] as Class<*>
            val value = (parameter.parameterizedType as ParameterizedType).actualTypeArguments[1] as Class<*>
            val exampleKey = EasyRandomUtil.easyRandom.nextObject(key)

            obj.put(key, value)
            return exampleKey
        }

        val test = Test::class.java.declaredMethods[0].kotlinFunction?.valueParameters
        fun createExampleObjClass(clazz: Class<*>): Any {
            return EasyRandomUtil.easyRandom.nextObject(clazz)
        }

        println(test?.get(0)?.type?.jvmErasure?.java?.let { createExampleObjClass(it) })
    }


    @Test
    fun `Поиск_всех_endpointов`(){

        class Test {
            @GetMapping("/v5")
            @Operation(summary = "Метод, который возвращает коллекцию со сложным объектом")
            fun returnCollectionComposite(model: Model ,film: Film, word: HashMap<User, Film>) : Map<User, Film> {
                return emptyMap()
            }

            @GetMapping("/doc")
            fun getDocumentation(model: Model): String {
                val endPointFinder = ApplicationEndpointsFinder(MethodParser(GenerationJsonExamplesEndpoint()))
                val generateDoc = DocumentationService(endPointFinder).buildDocumentation(model)
                return generateDoc
            }

        }

        val model = Test::class.java.declaredMethods[0]
        val model2 = Test::class.java.declaredMethods[1]

        val test = MethodParser(GenerationJsonExamplesEndpoint()).extractMethodInfo(model2).body

        println(test)


    }

    @Test
    fun `получение_примеров_полей_класса_с_easy_random`(){
        val classView = ClassParser().extractClassInfo(User::class.java)

        val example = EasyRandomUtil.easyRandom.nextObject(User::class.java.declaredFields.get(1).type).toString()

        fun getExampleField(clazz: Class<*>) {
            val fields = clazz.declaredFields
            val examples = fields.map { EasyRandomUtil.easyRandom.nextObject(it.type) }
            examples.forEach { println(it) }
        }

        println(classView)
    }
}
