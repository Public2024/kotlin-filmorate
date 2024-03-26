package com.example.doc.controllers


import com.example.doc.parsing.MethodParser
import com.example.doc.service.ApplicationEndpointsFinder
import com.example.doc.service.DocumentationService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class WebController {

    @GetMapping("/doc")
    fun getDocumentation(model: Model): String {
        val endPointFinder = ApplicationEndpointsFinder(MethodParser())
        val generateDoc = DocumentationService(endPointFinder).buildDocumentation(model)
        return generateDoc
    }

}