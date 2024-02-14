package com.example.javafilmoratekotlin.service

import com.example.javafilmoratekotlin.parsing.*
import com.example.javafilmoratekotlin.view.DocumentViewGenerator
import com.example.javafilmoratekotlin.view.DocumentViewType
import com.example.javafilmoratekotlin.view.DocumentationEndpoint
import com.example.javafilmoratekotlin.view.DocumentationSources
import org.springframework.stereotype.Service

@Service
class DocumentationService(
    private val endpointsFinder: ApplicationEndpointsFinder,
    private val methodParser: MethodParser,
    private val generators: List<DocumentViewGenerator>,
) {

    fun buildDocumentation(): Map<DocumentViewType, String> {
        val endpoints = endpointsFinder.findAllEndpoints()
        val documentedEndpoints = endpoints.map { DocumentationEndpoint(it, methodParser.extractClassInfo(it.method)) }

       return generators.associate { it.type to it.generate(DocumentationSources(documentedEndpoints)) }
    }

}