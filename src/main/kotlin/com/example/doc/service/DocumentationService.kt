package com.example.doc.service

import com.example.doc.view.*
import org.springframework.stereotype.Service
import org.springframework.ui.Model

@Service
class DocumentationService(
    private val endpointsFinder: ApplicationEndpointsFinder
) {
    /*Метод вывода документации*/
    fun buildDocumentation(model: Model): String {
        val endpoints = endpointsFinder.findAllEndpoints()
        val documentationEndpoint = endpoints.map { point ->
            DocumentationEndpoint(
                EndpointType(
                    type = point.type,
                    path = point.path
                ),
                MethodToDoc(
                    name = point.method.name,
                    description = point.method.description,
                    summary = point.method.summary,
                    parameters = point.method.parameters,
                    result = point.method.result,
                    classes = GetClassesOfEndpoint().getAllClassesRelatedToEndpoint(point.method),
                    body = point.method.body,
                    response = point.method.response
                )
            )
        }
        return DocumentViewGeneratorHtml().generate(documentationEndpoint, model)
    }
}

