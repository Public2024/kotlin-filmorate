package com.example.javafilmoratekotlin.service

import com.example.javafilmoratekotlin.view.DocumentViewType
import com.example.javafilmoratekotlin.view.DocumentationEndpoint
import com.example.javafilmoratekotlin.view.DocumentationSources
import com.example.javafilmoratekotlin.view.EndpointType
import org.springframework.stereotype.Service

@Service
class DocumentationServiceExample(private val endpointsFinder: ApplicationEndpointsFinder,) {

    fun buildDocumentation(): List<ApplicationEndpoint> {
        return endpointsFinder.findAllEndpoints()
    }

}