package com.example.doc.view

import com.example.doc.parsing.ClassView
import com.example.doc.parsing.InputParameter
import com.example.doc.parsing.OutputResult
import org.springframework.ui.Model

interface DocumentViewGenerator {
    /*    val type: DocumentViewType*/
    fun generate(endpoint: List<DocumentationEndpoint>, model: Model): String

}

/*Представление метода в HTML*/
data class MethodToDoc(
    val name: String,
    val description: String?,
    val summary: String?,
    val parameters: List<InputParameter>?,
    val result: OutputResult?,
    /*все классы относящиеся к endpoint*/
    val classes: List<ClassView>?,
    /*Примеры JSON*/
    val body: String?,
    val response: String?
)


data class DocumentationEndpoint(
    val endpoint: EndpointType,
    val view: MethodToDoc?,
)

/*data class для path*/
data class EndpointType(
     val type: String,
     val path: String,
)