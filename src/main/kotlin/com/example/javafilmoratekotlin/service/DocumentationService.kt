package com.example.javafilmoratekotlin.service

import com.example.javafilmoratekotlin.parsing.ClassView
import com.example.javafilmoratekotlin.parsing.InputParameter
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
            DocumentationEndpointNew (EndpointType(
                type = point.type,
                path = point.path),
            MethodToDoc(name =  point.method.name,
                description =  point.method.description,
                summary =  point.method.summary,
                parameters = getParameters(point.method.parameters),
                result = ResultOfMethod(
                    result = point.method.result,
                    classes = GetClassesRelatedToParameter().getAllClasses(point.method.result?.composite)
                )
            )
            )}
        return DocumentViewGeneratorHtml().generateNew(documentationEndpoint, model)
    }

    private fun getParameters(parameters: List<InputParameter>?): List<ParameterOfMethod>? {
        return parameters?.map { ParameterOfMethod(
            parameter = it,
            classes = GetClassesRelatedToParameter().getAllClasses(it.classView)
        ) }
    }

    /*Получение классов */
    private class GetClassesRelatedToParameter(){

        private val listOfClasses = mutableListOf<ClassView>()
        private fun findClassView(classView: ClassView?){
            classView?.fields?.forEach{ field -> if(field.classOfComposite!=null)
                listOfClasses.add(field.classOfComposite)
                findClassView(field.classOfComposite)
            }
        }
        fun getAllClasses(classView: ClassView?): List<ClassView>{
            classView?.let { listOfClasses.add(it) }
            findClassView(classView)
            return listOfClasses
        }
    }

}

