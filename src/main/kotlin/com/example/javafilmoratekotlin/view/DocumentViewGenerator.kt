package com.example.javafilmoratekotlin.view

import com.example.javafilmoratekotlin.parsing.MethodView
import com.example.javafilmoratekotlin.service.ApplicationEndpoint

interface DocumentViewGenerator {
    val type: DocumentViewType
    fun generate(request: DocumentationSources): String

}

enum class DocumentViewType {
    HTML, MARKDOWN
}
data class DocumentationSources(
    val endpoints: List<DocumentationEndpoint>,
    )

data class DocumentationEndpoint(
        val endpoint: EndpointType,
        val view: MethodView?,
)

data class EndpointType(
    val type: String,
    val path: String,
)