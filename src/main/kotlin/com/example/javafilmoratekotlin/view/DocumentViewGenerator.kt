package com.example.javafilmoratekotlin.view

import com.example.javafilmoratekotlin.parsing.ClassView
import com.example.javafilmoratekotlin.parsing.MethodView
import org.springframework.ui.Model

interface DocumentViewGenerator {
    /*    val type: DocumentViewType*/
    fun generate(endpoints: List<DocumentationEndpoint>, classes: List<ClassView>, model: Model): String
}

/*enum class DocumentViewType {
    HTML, MARKDOWN
}*/
/*data class DocumentationSources(
     val endpoints: List<DocumentationEndpoint>,
)*/


data class DocumentationEndpoint(
     val endpoint: EndpointType,
     val view: MethodView?,
)

data class EndpointType(
     val type: String,
     val path: String,
)