package com.example.javafilmoratekotlin.view

import org.springframework.stereotype.Component
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

@Component
class DocumentViewGeneratorHtml : DocumentViewGenerator {
    override val type = DocumentViewType.HTML
    var headers: List<String> = listOf("ID", "Name", "Salary", "Status")
    var rows = mutableMapOf<String, String>()

    /*Сюда необходимо вставить HTML таблица*/
    override fun generate(request: DocumentationSources): String {
        val rowOne = mapOf("ID" to "1", "Name" to "Jim", "Salary" to "50000", "Status" to "active")
        val rowTwo = mapOf("ID" to "2", "Name" to "Sally", "Salary" to "50000", "Status" to "inactive")
        rows.putAll(rowOne)
        rows.putAll(rowTwo)

        return """     
                
  <!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Spring Boot Thymeleaf kotlin-filmorate</title>
</head>
<body>
    <table>
    <thead>
        <tr class="headings">
            <th th:each="header: ${headers}" class="column-title" th:text="${headers.listIterator()}" />
        </tr>
    </thead>

    <tbody>
        <tr class="even pointer" th:each="row: ${rows}" id="tablerow">
            <td th:each="header: ${headers}" th:text="${rows}" />
        </tr>
    </tbody>
</table>
</body>
</html>
"""
    }
}