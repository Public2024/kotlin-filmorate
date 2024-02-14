package com.example.javafilmoratekotlin.service

import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping
import java.lang.reflect.Method

@Component
class ApplicationEndpointsFinder(private val context: ApplicationContext) {

    fun findAllEndpoints(): List<ApplicationEndpoint> {

        val requestMappingHandlerMapping = context
            .getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping::class.java)
        val restmethodsByPath = requestMappingHandlerMapping.handlerMethods

        return emptyList()
    }
}

data class ApplicationEndpoint(
    val type: ApplicationEndpointType,
    // откуда вызывается
    val path: String,

    val method: Method,
)


enum class ApplicationEndpointType {
    REST
}