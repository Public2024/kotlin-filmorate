package com.example.javafilmoratekotlin.view

import com.example.javafilmoratekotlin.parsing.ClassParser
import com.example.javafilmoratekotlin.parsing.MethodParser
import com.example.javafilmoratekotlin.service.ApplicationEndpointsFinder
import com.example.javafilmoratekotlin.service.DocumentationService
import org.springframework.stereotype.Component

@Component
class DocumentViewGeneratorHtml : DocumentViewGenerator {
    override val type = DocumentViewType.HTML
    override fun generate(request: DocumentationSources): String {
        /*Предполагается что здесь documentation service*/
        TODO("Not yet implemented")
    }
}