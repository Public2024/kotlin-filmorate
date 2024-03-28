package com.example.doc.service

import com.example.doc.util.EasyRandomUtil
import com.example.doc.util.SerializationUtil
import com.example.doc.util.TypeSeparator
import com.example.doc.util.TypeSeparator.Companion.findCollectionWords
import org.genthz.dasha.DashaObjectFactory
import java.lang.reflect.Parameter
import kotlin.reflect.KType
import kotlin.reflect.jvm.jvmErasure

class GenerationJsonExamplesEndpoint {

    /*Json представления параметров (body) endpoint */
    fun getJsonOfBody(parameters: Array<Parameter?>): String {
        val obj = mutableMapOf<String, Any>()
        if(parameters.isNotEmpty()) {
            parameters.forEach { val pair = getExampleParameter(it!!); obj[pair.first] = pair.second }
            return toJson(obj)
        } else {
            return ""
        }
    }

    /*Json представление объекта (response) результата запроса к endpoint */
    fun getJsonOfResponse(response: KType): String {
        if(response.toString() == "kotlin.Unit") return ""
        return toJson(getExampleResult(response))
    }

    /*Получение примера объекта для результата*/
    private fun getExampleResult(response: KType): Any {
        val clazz = response.jvmErasure.java
        val forList = response.arguments.first().type?.jvmErasure?.java
        return when {
            TypeSeparator.isCollectionSingle(clazz) -> createExampleCollectionClass(forList!!)
            TypeSeparator.isCollectionMap(clazz) -> createExampleMapClassResponse(response)
            else -> {
                createExampleObjClass(clazz)
            }
        }
    }

    /*Получение примера объекта для параметра */
    private fun getExampleParameter(parameter: Parameter): Pair<String, Any> {
        val name = parameter.name
        return when {
            TypeSeparator.isCollectionSingle(parameter.type) -> {
                val clazz = TypeSeparator.getObjCollectionSingleForParam(parameter)
                return Pair(name, createExampleCollectionClass(clazz))
            }

            TypeSeparator.isCollectionMap(parameter.type) ->
                Pair(name, createExampleMapClass(parameter))

            else -> {
                return Pair(name, createExampleObjClass(parameter.type))
            }
        }
    }

    /*Сериализация объекта в Json */
    private fun toJson(obj: Any): String {
        return SerializationUtil.globalJsonMapper.writeValueAsString(obj)
    }

    /*Получение example Map объекта result*/
    private fun createExampleMapClassResponse(type: KType): Any {
        val pair = TypeSeparator.getObjMapForResult(type)
        return if (pair.toString().contains(findCollectionWords)) {
            "{[]}"
        } else {
            DashaObjectFactory().get(
                 Map::class.java,
                 pair.first,
                 pair.second
            ).firstNotNullOf { it }
        }
    }

    /*Получение example Map объекта параметра*/
    private fun createExampleMapClass(parameter: Parameter): Any {
        val pair = TypeSeparator.getObjMapForParam(parameter)
        return if (pair.toString().contains(findCollectionWords)) {
            "{[]}"
        } else {
            DashaObjectFactory().get(
                 Map::class.java,
                 pair.first,
                 pair.second
            ).firstNotNullOf { it }
        }
    }

    /*Получение example Collection объекта*/
    private fun createExampleCollectionClass(clazz: Class<*>): Any {
        return EasyRandomUtil.easyRandom
             .objects(clazz, 1)
             .toList()
    }

    /*Получение example composite объекта*/
    private fun createExampleObjClass(clazz: Class<*>): Any {
        return EasyRandomUtil.easyRandom.nextObject(clazz)
    }

}