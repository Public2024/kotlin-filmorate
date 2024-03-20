package com.example.javafilmoratekotlin.service

import com.example.javafilmoratekotlin.util.ClassViewRecorder
import com.example.javafilmoratekotlin.view.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.ui.Model

@Service
class DocumentationService(
    private val endpointsFinder: ApplicationEndpointsFinder
) {
    fun buildDocumentation(model: Model): String {
        val endpoints = endpointsFinder.findAllEndpoints()
        val documentedEndpoints = endpoints.map { DocumentationEndpoint(EndpointType(it.type, it.path), it.method) }
        val dataClasses = ClassViewRecorder.getRecorder()
        return DocumentViewGeneratorHtml().generate(documentedEndpoints, dataClasses, model)
    }

}