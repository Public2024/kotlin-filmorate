package com.example.doc.service

import com.example.doc.util.EasyRandomUtil
import com.example.doc.util.SerializationUtil
import java.lang.reflect.Parameter

class GenerationJsonExamplesEndpoint {

    /*Json представления параметров endpoint (body)*/
    fun getJsonOfBody(parameters: Array<Parameter>): String {
        val obj = mutableMapOf<String, Any>()
        parameters.forEach { obj[it.name] = createExampleClass(it.type)}
        return  toJson(obj)
    }

    /*Json представление объекта результата запроса к endpoint (response)*/
    fun getJsonOfResponse(response: Class<*>): String{
        return ""
    }

    /*Получение примера объекта*/
    fun createExampleClass(clazz: Class<*>): Any {
        val exampleObject = EasyRandomUtil.easyRandom.objects(clazz, 1).toList().first()
        return exampleObject
    }

    /*Сериализация объекта в Json */
    fun toJson(obj: Any): String {
        return SerializationUtil.globalJsonMapper.writeValueAsString(obj)
    }

}