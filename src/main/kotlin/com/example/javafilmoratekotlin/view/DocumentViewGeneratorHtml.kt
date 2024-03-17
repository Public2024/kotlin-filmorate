package com.example.javafilmoratekotlin.view

import org.springframework.stereotype.Component
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping


@Component
class DocumentViewGeneratorHtml : DocumentViewGenerator {
/*
    override val type = DocumentViewType.HTML*/

    /*Возврат таблицы HTML*/
    override fun generate(request: List<DocumentationEndpoint>, model: Model): String {
        model.addAttribute("DocumentationEndpoint", request)
        return "index"
    }
}
