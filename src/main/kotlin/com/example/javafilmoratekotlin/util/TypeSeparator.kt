package com.example.javafilmoratekotlin.util

import java.lang.reflect.Field
import java.lang.reflect.ParameterizedType
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*



class TypeSeparator {
    companion object {
        private val primitivesJava = listOf<Class<*>>(
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

        private fun getPrimitiveTypesClass(clazz: Class<*>): Boolean {
            return primitivesJava.contains(clazz)
        }

        fun getPrimitiveTypes(field: Field): Boolean {
            return primitivesJava.contains(field.type)
        }

        fun getCollectionTypes(field: Field): Boolean {
            return collections.contains(field.type)
        }

        fun isCollection(clazz: Class<*>): Boolean {
            return collections.contains(clazz) ?: collections.contains(clazz.superclass) ?: false
        }

        fun isPrimitive(clazz: Class<*>): Boolean {
            return primitivesJava.contains(clazz) ?: primitivesJava.contains(clazz.superclass)
            ?: false
        }

        fun checkingOnPrimitiveCollection(field: Field): Boolean {
            val getObjectCollection = (field.genericType as ParameterizedType).actualTypeArguments.first() as Class<*>
            return getPrimitiveTypesClass(getObjectCollection)
        }
    }
}