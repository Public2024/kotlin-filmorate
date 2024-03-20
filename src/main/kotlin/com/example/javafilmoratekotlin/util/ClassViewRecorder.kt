package com.example.javafilmoratekotlin.util

import com.example.javafilmoratekotlin.parsing.ClassView
import org.springframework.stereotype.Component

@Component
object ClassViewRecorder {

    private var classViewRecorder = mutableListOf<ClassView>()

    fun addIntoClassRecorder(classView: ClassView) {
        classViewRecorder.add(classView)
    }

    fun checkClassRecorder(name: String): Boolean {
        val names = classViewRecorder.map { it.simpleName }
        return names.contains(name)
    }

    fun getRecorder(): List<ClassView> {
        return classViewRecorder
    }
}