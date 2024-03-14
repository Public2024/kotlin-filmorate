package com.example.javafilmoratekotlin.service

import com.example.javafilmoratekotlin.view.*
import org.springframework.stereotype.Service

@Service
class DocumentationService(
    private val endpointsFinder: ApplicationEndpointsFinder,
    private val generators: List<DocumentViewGenerator>
) {

    fun buildDocumentation(): Map<DocumentViewType, String> {
        val endpoints = endpointsFinder.findAllEndpoints()
        val documentedEndpoints = endpoints.map { DocumentationEndpoint(EndpointType(it.type, it.path), it.method) }
        return generators.associate { it.type to it.generate(DocumentationSources(documentedEndpoints)) }
    }

}