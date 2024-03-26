package com.example.doc.service


import com.example.doc.parsing.MethodParser
import com.example.doc.parsing.MethodView
import com.example.doc.util.ApplicationContextProviders
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.mvc.method.RequestMappingInfo
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping


@Component
class ApplicationEndpointsFinder(private val methodParser : MethodParser) {

    /*Поиск всех энедпоинтов приложения*/
    fun findAllEndpoints(): List<ApplicationEndpoint> {
        val requestMappingHandlerMapping = ApplicationContextProviders.getApplicationContext()
             .getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping::class.java)
        val restMethodsByPath = requestMappingHandlerMapping.handlerMethods
        val filteredEndpoint = filterAndSortedEndpoints(restMethodsByPath)
        return filteredEndpoint.map { getApplicationEndpoint(it) }
    }

    /** Сортировка и фильтр списка всех эндпоинтов*/
    private fun filterAndSortedEndpoints(
         methods: Map<RequestMappingInfo, HandlerMethod>
    ): Map<RequestMappingInfo, HandlerMethod> {
        val filterMethods = methods.filterNot {
            it.key.toString().contains(ApplicationContextProviders.deleteExtraEndpoints)
        }
        return filterMethods
             .toSortedMap(compareBy(String.CASE_INSENSITIVE_ORDER) { it.toString() })
    }

    /*Парсинг эндпоинта*/
    private fun getApplicationEndpoint(entry: Map.Entry<RequestMappingInfo, HandlerMethod>): ApplicationEndpoint {
        val endpoint = ApplicationContextProviders.deleteBrackets.replace(entry.key.toString(), "")
        val (type, path) = endpoint.split(" ")
        return ApplicationEndpoint(
             type = type,
             path = path,
             method = methodParser.extractMethodInfo(entry.value.method)
        )
    }
}

data class ApplicationEndpoint(
     val type: String,
     // откуда вызывается
     val path: String,
     val method: MethodView,
)
