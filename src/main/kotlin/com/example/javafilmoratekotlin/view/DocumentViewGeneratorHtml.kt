package com.example.javafilmoratekotlin.view


import org.springframework.stereotype.Component
import org.springframework.ui.Model


@Component
class DocumentViewGeneratorHtml : DocumentViewGenerator {

    /*Возврат таблицы HTML*/
    override fun generate(endpoint: List<DocumentationEndpoint>, model: Model): String {
        model.addAttribute("DocumentationEndpoint", endpoint)
        return "index"
    }

}





