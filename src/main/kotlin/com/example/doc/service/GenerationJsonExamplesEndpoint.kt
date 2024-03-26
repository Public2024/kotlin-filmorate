package com.example.doc.service

import com.example.doc.util.EasyRandomUtil
import com.example.doc.util.SerializationUtil
import java.lang.reflect.Parameter

class GenerationJsonExamplesEndpoint {

    fun getJsonOfParameters(parameters: Array<Parameter>): String {
        val obj = mutableMapOf<String, String>()
        parameters.forEach { obj[it.name] = toJson(createExampleClass(it.type))}
        return  toJson(obj)
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