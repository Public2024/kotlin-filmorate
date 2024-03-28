package com.example.doc.service

import com.example.doc.util.EasyRandomUtil
import com.example.doc.util.SerializationUtil
import com.example.doc.util.TypeSeparator
import com.example.doc.util.TypeSeparator.Companion.findCollectionWords
import io.micrometer.core.ipc.http.HttpSender.Response
import org.genthz.dasha.DashaObjectFactory
import java.lang.reflect.Parameter
import java.lang.reflect.Type
import kotlin.reflect.KType
import kotlin.reflect.jvm.jvmErasure

class GenerationJsonExamplesEndpoint {

    /*Json представления параметров (body) endpoint */
    fun getJsonOfBody(parameters: Array<Parameter>): String {
        val obj = mutableMapOf<String, Any>()
        parameters.forEach { val pair = getExampleParameter(it); obj[pair.first] = pair.second }
        return toJson(obj)
    }

    /*Json представление объекта (response) результата запроса к endpoint */
    fun getJsonOfResponse(response: Class<*>): String {
        return ""
    }

    /*Получение примера объекта для результата*/
    private fun getExampleResult(response: KType): Any {
        val clazz = response.jvmErasure.java
         return when {
             TypeSeparator.isCollectionSingle(clazz) -> createExampleCollectionClass(clazz)
             TypeSeparator.isCollectionMap(clazz) -> createExampleMapClassResponse(response)
             else -> {createExampleObjClass(clazz)}
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
            else -> {return Pair(name, createExampleObjClass(parameter.type))}
        }
    }

    /*Сериализация объекта в Json */
    private fun toJson(obj: Any): String {
        return SerializationUtil.globalJsonMapper.writeValueAsString(obj)
    }


    /*Получение example Map объекта result*/
    private fun createExampleMapClassResponse(type: KType): Any{
        val pair = TypeSeparator.getObjMapForResult(type)
        return if(pair.toString().contains(findCollectionWords)){
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
        return if(pair.toString().contains(findCollectionWords)){
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