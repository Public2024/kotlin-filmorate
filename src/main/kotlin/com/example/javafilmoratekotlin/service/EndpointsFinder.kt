package com.example.javafilmoratekotlin.service

import com.example.javafilmoratekotlin.parsing.MethodParser
import com.example.javafilmoratekotlin.parsing.MethodView
import org.springframework.context.ApplicationContext
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.mvc.method.RequestMappingInfo
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping


@Component
class ApplicationEndpointsFinder(
    private val context: ApplicationContext,
    private val methodParser: MethodParser,
) {

    private final val applicationContext = context

    val event = ContextRefreshedEvent(applicationContext)

    //TODO: скорее всего должен сам уметь найти пакет текущего приложения
    /* Поиск все эндпоинтов в пакете*/
    fun findAllEndpoints(packageName: String): List<ApplicationEndpoint> {
        val context = event.applicationContext
        val requestMappingHandlerMapping = context
            .getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping::class.java)
        val restMethodsByPath = requestMappingHandlerMapping.handlerMethods
        val filteredEndpoint = filterAndSortedEndpoints(restMethodsByPath, packageName)
        return filteredEndpoint.map { getApplicationEndpoint(it) }
    }

    /*Сортировка и фильтр списка всех эндпоинтов*/
    private fun filterAndSortedEndpoints(
        methods: Map<RequestMappingInfo, HandlerMethod>,
        packageName: String
    ): Map<RequestMappingInfo, HandlerMethod> {
        val filterMethods = methods.filter {
            it.value.toString().contains(packageName, ignoreCase = true)
        }
        return filterMethods
            .toSortedMap(compareBy(String.CASE_INSENSITIVE_ORDER) { it.toString() })
    }


    /*Парсинг эндпоинта*/
    private fun getApplicationEndpoint(entry: Map.Entry<RequestMappingInfo, HandlerMethod>): ApplicationEndpoint {

        val endpoint = regex.replace(entry.key.toString(), "")
        val (type, path) = endpoint.split(" ")
        return ApplicationEndpoint(
            type = type,
            path = path,
            method = methodParser.extractMethodInfo(entry.value.method)
        )
    }

    companion object {
        //TODO: перенезвать понятно или напистаь что делает
        private val regex = """[{}\]\[]""".toRegex()
    }

}

data class ApplicationEndpoint(
    val type: String,
    // откуда вызывается
    val path: String,

    val method: MethodView,
)

enum class ApplicationEndpointType {
    POST,
    GET,
    PUT,
    DELETE
}