package com.example.javafilmoratekotlin.util

import java.lang.reflect.Field
import java.lang.reflect.Type
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class TypeSeparator {
    private val primitives = mapOf<Class<*>, Boolean>(
        Int::class.javaObjectType to true,
        Boolean::class.javaObjectType to true,
        Double::class.javaObjectType to true,
        Float::class.javaObjectType to true,
        Integer::class.javaObjectType to true,
        Long::class.javaObjectType to true,
        BigDecimal::class.javaObjectType to true,

        String::class.javaObjectType to true,
        LocalDateTime::class.javaObjectType to true,
        LocalDate::class.javaObjectType to true,
        Instant::class.javaObjectType to true,

        UUID::class.javaObjectType to true,

        Int::class.java to true,
        Boolean::class.java to true,
        Double::class.java to true,
        Float::class.java to true,
        Integer::class.java to true,
        Long::class.java to true,
        BigDecimal::class.java to true,

        String::class.java to true,
        LocalDateTime::class.java to true,
        LocalDate::class.java to true,
        Instant::class.java to true,

        UUID::class.java to true,
    )

    private val collections = mapOf<Class<*>, Boolean>(
        Collection::class.javaObjectType to true,
        List::class.javaObjectType to true,
        Array::class.javaObjectType to true,
        Set::class.javaObjectType to true,

        Map::class.javaObjectType to true,

        Collection::class.java to true,
        List::class.java to true,
        Array::class.java to true,
        Set::class.java to true,

        Map::class.java to true,
    )

    fun getPrimitiveTypesClass(clazz: Class<*>): Boolean{
        return primitives.containsKey(clazz)
    }

    fun getCollectionTypesClass(clazz: Class<*>): Boolean{
        return primitives.containsKey(clazz)
    }

    fun getPrimitiveTypes(field: Field): Boolean {
        return primitives.containsKey(field.type)
    }

    fun getCollectionTypes(field: Field): Boolean {
        return collections.containsKey(field.type)
    }


}