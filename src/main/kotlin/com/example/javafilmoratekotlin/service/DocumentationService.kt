package com.example.javafilmoratekotlin.service

import com.example.javafilmoratekotlin.parsing.ClassView
import com.example.javafilmoratekotlin.parsing.InputParameter
import com.example.javafilmoratekotlin.parsing.MethodView
import com.example.javafilmoratekotlin.parsing.OutputResult
import com.example.javafilmoratekotlin.util.ClassViewRecorder
import com.example.javafilmoratekotlin.view.*
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

    /*Метод для документации*/
    fun buildDocumentationNew(model: Model): String {
        val endpoints = endpointsFinder.findAllEndpoints()
        val documentationEndpoint = endpoints.map { point ->
            DocumentationEndpointNew(
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
                    classes = getAllClassesRelatedToEndpoint(point.method)
                )
            )
        }
        return DocumentViewGeneratorHtml().generateNew(documentationEndpoint, model)
    }

    /*
    * Для тестов (потом надо DELETE)*/
    fun testBuild(): List<DocumentationEndpointNew> {
        val endpoints = endpointsFinder.findAllEndpoints()
        val documentationEndpoint = endpoints.map { point ->
            DocumentationEndpointNew(
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
                    classes = getAllClassesRelatedToEndpoint(point.method)
                )
            )
        }
        return documentationEndpoint
    }

    /*
    * Получение всех уникальных классов относящихся к endpoint */
    fun getAllClassesRelatedToEndpoint(method: MethodView): List<ClassView>? {
        val classes = mutableListOf<ClassView>()
        classes.addAll(getClassesOfParameter(method.parameters))
        classes.addAll(getClassesOfResult(method.result))
        return classes.distinctBy { it.name }
    }

    /*
    *Получение классов относящихся к параметрам endpoint */
    private fun getClassesOfParameter(parameter: List<InputParameter>?): List<ClassView> {
        val classes = mutableListOf<ClassView>()
        parameter?.forEach { classes.addAll(GetClassesRelatedToParameter().getAllClasses(it.classView)) }
        return classes
    }

    /*
    * Получение классов относящихся к результату endpoint*/
    private fun getClassesOfResult(result: OutputResult?): List<ClassView> {
        return GetClassesRelatedToParameter().getAllClasses(result?.composite)
    }


    /*Получение вложенные ClassView относящихся к параметру или результату endpoint*/
    private class GetClassesRelatedToParameter() {
        private val listOfClasses = mutableListOf<ClassView>()
        private fun findClassView(classView: ClassView?) {
            classView?.fields?.forEach { field ->
                if (field.classOfComposite != null)
                    listOfClasses.add(field.classOfComposite)
                findClassView(field.classOfComposite)
            }
        }

        fun getAllClasses(classView: ClassView?): List<ClassView> {
            classView?.let { listOfClasses.add(it) }
            findClassView(classView)
            return listOfClasses
        }
    }

}

