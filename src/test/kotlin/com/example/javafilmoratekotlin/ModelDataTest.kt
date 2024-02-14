package com.example.javafilmoratekotlin

import com.example.javafilmoratekotlin.model.Film
import com.example.javafilmoratekotlin.parsing.ClassParser
import org.junit.jupiter.api.Test


class ModelDataTest {

    private val parser = ClassParser()

    @Test
    fun test(){
        println(parser.extractClassInfo(Film::class.java).simpleName)
        println(parser.extractClassInfo(Film::class.java).primitives)
        println(parser.extractClassInfo(Film::class.java).enum)
        println(parser.extractClassInfo(Film::class.java).unique)
        println(parser.extractClassInfo(Film::class.java).collectionPrimitive)
        println(parser.extractClassInfo(Film::class.java).collectionUnique)
    }

}