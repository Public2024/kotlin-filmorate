package com.example.javafilmoratekotlin.view


import kotlinx.html.*
import kotlinx.html.stream.createHTML
import org.springframework.stereotype.Component
import org.springframework.ui.Model


@Component
class DocumentViewGeneratorHtml : DocumentViewGenerator {
/*
    override val type = DocumentViewType.HTML*/

    /*Возврат таблицы HTML*/
    override fun generate(endpoint: List<DocumentationEndpoint>, model: Model): String {
        model.addAttribute("DocumentationEndpoint", endpoint)
        return "index"
    }

    override fun generateNew(endpoint: List<DocumentationEndpoint>): String {
        return createHTML()
             .html {
                 body {
                     table {
                         thead() {
                             tr {
                                 td {
                                     +"Тип"
                                 }
                                 td {
                                     +"Путь"
                                 }
                             }
                         }
                         val endpoints = endpoint
                         for (points in endpoints ) {
                             tr {
                                 td {
                                     text(points.endpoint.type)
                                 }
                                 td {
                                     text(points.endpoint.path)
                                 }
                             }

                         }
                     }
                 }
             }
    }
    }


    /*https://gist.github.com/2sbsbsb/2951464*/
    /*https://github.com/JakeWharton/picnic*/
    /* https://habr.com/ru/articles/422083/ */





