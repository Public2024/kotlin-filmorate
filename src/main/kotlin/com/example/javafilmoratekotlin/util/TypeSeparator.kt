package com.example.javafilmoratekotlin.util

import java.lang.reflect.Field
import java.lang.reflect.ParameterizedType
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import kotlin.reflect.KType
import kotlin.reflect.jvm.javaType
import kotlin.reflect.jvm.jvmErasure

class TypeSeparator {
    companion object {
        private val primitivesObj = listOf<Class<*>>(
            Int::class.javaObjectType,
            Boolean::class.javaObjectType,
            Double::class.javaObjectType,
            Float::class.javaObjectType,
            Integer::class.javaObjectType,
            Long::class.javaObjectType,
            BigDecimal::class.javaObjectType,

            String::class.javaObjectType,
            LocalDateTime::class.javaObjectType,
            LocalDate::class.javaObjectType,
            Instant::class.javaObjectType,

            UUID::class.javaObjectType,
            Unit::class.javaObjectType,

            Int::class.java,
            Boolean::class.java,
            Double::class.java,
            Float::class.java,
            Integer::class.java,
            Long::class.java,
            BigDecimal::class.java,

            String::class.java,
            LocalDateTime::class.java,
            LocalDate::class.java,
            Instant::class.java,

            UUID::class.java,
            Unit::class.java

        )

        private val collections = listOf<Class<*>>(
            Collection::class.javaObjectType,
            List::class.javaObjectType,
            Array::class.javaObjectType,
            Set::class.javaObjectType,
            ArrayList::class.java,

            Map::class.javaObjectType,
            HashMap::class.javaObjectType,

            Collection::class.java,
            List::class.java,
            Array::class.java,
            ArrayList::class.java,
            Set::class.java,

            Map::class.java,
            HashMap::class.java
        )

        fun isPrimitiveTypes(field: Field): Boolean {
            return primitivesObj.contains(field.type)
        }

        fun isCollectionTypes(field: Field): Boolean {
            return collections.contains(field.type)
        }

        fun isCollection(clazz: Class<*>): Boolean {
            return collections.contains(clazz)
        }

        fun isPrimitive(clazz: Class<*>): Boolean {
            return primitivesObj.contains(clazz)
        }

        /*Для получения объекта коллекции*/
        fun getObjCollection(field: Field): Class<*> {
            return (field.genericType as ParameterizedType).actualTypeArguments.first() as Class<*>
        }

        private val findCollectionWords = Regex("(Collection)|(List)|(ArrayList)|(Set)|(HashMap)|(Map)")

        private fun correctTypeName(type: List<String>): String{
            return type.filter { it.contains(Regex("[A-Z]")) }.map { it.replace(",", ";") }
                .toString().replace(findCollectionWords, "Коллекция<")
                .replace("""[,\]\[]""".toRegex(), "").replace(" ", "")
        }

        /*
        Для вывода типа объекта полей класса для документации*/
        fun parseFieldTypeName(field: Field): String {
            if (!TypeSeparator.isCollectionTypes(field)) return field.type.simpleName
            val type = field.genericType.typeName.split(".", "<", " ")
            return correctTypeName(type)
        }

        /*
        Для вывода типа объекта параметров и результата метода(endpointa) для документации*/
        fun parseParameterTypeName(type: KType): String {
            if (!TypeSeparator.isCollection(type.jvmErasure.java)) return type.javaType.typeName.substringAfterLast(".")
            val type = type.javaType.typeName.split(".", "<", " ")
            return correctTypeName(type)

        }

    }
}