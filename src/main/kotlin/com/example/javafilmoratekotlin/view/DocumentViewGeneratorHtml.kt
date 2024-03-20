package com.example.javafilmoratekotlin.view

import com.example.javafilmoratekotlin.parsing.ClassView
import org.springframework.stereotype.Component
import org.springframework.ui.Model

@Component
class DocumentViewGeneratorHtml : DocumentViewGenerator {
/*
    override val type = DocumentViewType.HTML*/

    /*Возврат таблицы HTML*/
    override fun generate(endpoints: List<DocumentationEndpoint>, classes: List<ClassView>, model: Model): String {
        model.addAttribute("DocumentationEndpoint", endpoints)
        model.addAttribute("DataClasses", classes)
        return "index"
    }

    override fun generateNew(endpoint: List<DocumentationEndpointNew>, model: Model): String {
        model.addAttribute("Endpoints", endpoint)
        return "index2"
    }
}
