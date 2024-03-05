package com.example.javafilmoratekotlin.service

import com.example.javafilmoratekotlin.parsing.ClassParser
import com.example.javafilmoratekotlin.parsing.MethodParser
import org.springframework.context.ApplicationContext
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.mvc.method.RequestMappingInfo
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping
import java.lang.reflect.Method


@Component
class ApplicationEndpointsFinder(private val context: ApplicationContext) {

    private final val applicationContext = context

    val event = ContextRefreshedEvent(applicationContext)
    val methodParser = MethodParser(ClassParser())

    /* Поиск все эндпоинтов в пакете*/
    fun findAllEndpoints(path: String): List<ApplicationEndpoint> {
        val context = event.applicationContext
        val requestMappingHandlerMapping = context
                .getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping::class.java)
        val restMethodsByPath = requestMappingHandlerMapping.handlerMethods
        val filteredEndpoint = filterAndSortedEndpoints(restMethodsByPath, path)
        return filteredEndpoint.map { getApplicationEndpoint(it) }
    }

    /*Сортировка и фильтр списка всех эндпоинтов*/
    private fun filterAndSortedEndpoints(methods: MutableMap<RequestMappingInfo, HandlerMethod>, path: String):
            Map<RequestMappingInfo, HandlerMethod> {
        val filterMethods = methods.filter {
            it.value.toString()
                    .contains(path, ignoreCase = true)
        }
        return filterMethods
                .toSortedMap(compareBy(String.CASE_INSENSITIVE_ORDER) { it.toString() })
    }

    /*Парсинг эндпоинта*/
    private fun getApplicationEndpoint(entry: Map.Entry<RequestMappingInfo, HandlerMethod>):
            ApplicationEndpoint {
        val (type, path) = entry.key.toString().split(" ")
        return ApplicationEndpoint(
                type = type,
                path = path,
                method = entry.value.method
        )
    }
}

data class ApplicationEndpoint(
        val type: String,
        // откуда вызывается
        val path: String,

        val method: Method,
)

enum class ApplicationEndpointType {
    POST,
    GET,
    PUT,
    DELETE
}