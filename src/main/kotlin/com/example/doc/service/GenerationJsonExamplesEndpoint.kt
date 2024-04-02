package com.example.doc.service

import com.example.doc.util.EasyRandomUtil
import com.example.doc.util.SerializationUtil
import com.example.doc.util.TypeSeparator
import com.example.doc.util.TypeSeparator.Companion.findCollectionWords
import org.genthz.dasha.DashaObjectFactory
import org.springframework.stereotype.Service
import kotlin.reflect.KParameter
import kotlin.reflect.KType
import kotlin.reflect.jvm.jvmErasure

@Service
class GenerationJsonExamplesEndpoint {

    /*Json представления параметров (body) endpoint */
    fun getJsonOfBody(parameters: List<KParameter>): String {
        val obj = mutableMapOf<String, Any>()
        if(parameters.isNotEmpty()) {
            parameters.forEach { obj.put(it.name!!, getExample(it.type)) }
            return toJson(obj)
        } else {
            return ""
        }
    }


    /*Json представление объекта (response) результата запроса к endpoint */
    fun getJsonOfResponse(response: KType): String {
        if(response.toString() == "kotlin.Unit") return ""
        return toJson(getExample(response))
    }

    /*Получение примера объекта*/
    private fun getExample(response: KType): Any {
        val clazz = response.jvmErasure.java
        return when {
            TypeSeparator.isCollectionSingle(clazz) -> {
                val forList = response.arguments.first().type?.jvmErasure?.java
                createExampleCollectionClass(forList!!)}
            TypeSeparator.isCollectionMap(clazz) -> createExampleMapClassResponse(response)
            else -> {
                createExampleObjClass(clazz)
            }
        }
    }

    /*Сериализация объекта в Json */
    private fun toJson(obj: Any): String {
        return SerializationUtil.globalJsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj)
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

    /*Получение example Collection объекта*/
    private fun createExampleCollectionClass(clazz: Class<*>): Any {
        return EasyRandomUtil.easyRandom
             .objects(clazz, 1)
             .toList() ?: ""
    }

    /*Получение example composite объекта*/
    private fun createExampleObjClass(clazz: Class<*>): Any {
        return EasyRandomUtil.easyRandom.nextObject(clazz) ?: ""
    }

}