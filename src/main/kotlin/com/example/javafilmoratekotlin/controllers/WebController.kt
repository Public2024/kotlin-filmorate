package com.example.javafilmoratekotlin.controllers


import com.example.javafilmoratekotlin.parsing.MethodParser
import com.example.javafilmoratekotlin.service.ApplicationEndpointsFinder
import com.example.javafilmoratekotlin.service.DocumentationService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class WebController {

    @GetMapping("/doc")
    fun getDocumentation(model: Model): String {
        val endPointFinder = ApplicationEndpointsFinder(MethodParser())
        val generateDoc = DocumentationService(endPointFinder).buildDocumentation(model)
        return generateDoc
    }

    @RequestMapping("/doc_new")
    @ResponseBody
    fun getDocumentationNew(): String {
        val endPointFinder = ApplicationEndpointsFinder(MethodParser())
        val generateDoc = DocumentationService(endPointFinder).buildDocumentationNew()
        return generateDoc
    }

}