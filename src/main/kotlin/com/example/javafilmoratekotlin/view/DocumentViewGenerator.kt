package com.example.javafilmoratekotlin.view

import com.example.javafilmoratekotlin.parsing.ClassView
import com.example.javafilmoratekotlin.parsing.InputParameter
import com.example.javafilmoratekotlin.parsing.MethodView
import com.example.javafilmoratekotlin.parsing.OutputResult
import org.springframework.ui.Model

interface DocumentViewGenerator {
    /*    val type: DocumentViewType*/
    fun generate(endpoints: List<DocumentationEndpoint>, classes: List<ClassView>, model: Model): String
    fun generateNew(endpoint: List<DocumentationEndpointNew>, model: Model): String
}

/*enum class DocumentViewType {
    HTML, MARKDOWN
}*/
/*data class DocumentationSources(
     val endpoints: List<DocumentationEndpoint>,
)*/


/*Представление метода в HTML*/
data class MethodToDoc(
    val name: String,
    val description: String?,
    val summary: String?,
    val parameters: List<ParameterOfMethod>?,
    val result: ResultOfMethod?
)

/*П*/
data class ParameterOfMethod(
    val parameter: InputParameter?,
    /*поле если параметр композитный класс (с вложенными классами в него)*/
    val classes: List<ClassView>?
)

data class ResultOfMethod(
    val result: OutputResult?,
    /*поле если результат метода композитный класс(с вложенными классами в него)*/
    val classes: List<ClassView>?
)

data class DocumentationEndpointNew(
    val endpoint: EndpointType,
    val view: MethodToDoc?,
)

data class DocumentationEndpoint(
     val endpoint: EndpointType,
     val view: MethodView?,
)

/*для EndPoint*/
data class EndpointType(
     val type: String,
     val path: String,
)