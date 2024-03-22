package com.example.javafilmoratekotlin.controllers


import com.example.javafilmoratekotlin.parsing.ClassParser
import com.example.javafilmoratekotlin.parsing.MethodParser
import com.example.javafilmoratekotlin.service.ApplicationEndpoint
import com.example.javafilmoratekotlin.service.ApplicationEndpointsFinder
import com.example.javafilmoratekotlin.service.DocumentationService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class WebController {

/*    @GetMapping("/doc")
    fun getDocumentation(model: Model): String {
        val endPointFinder = ApplicationEndpointsFinder(MethodParser())
        val generateDoc = DocumentationService(endPointFinder).buildDocumentation(model)
        return generateDoc
    }*/

    @GetMapping("/doc_new")
    fun getDocumentationNew(model: Model): String {
        val endPointFinder = ApplicationEndpointsFinder(MethodParser())
        val generateDoc = DocumentationService(endPointFinder).buildDocumentationNew(model)
        return generateDoc
    }




}

/* @GetMapping("/HTML")
 fun generateHTML(): String {
     var headers: List<String> = listOf("ID", "Name", "Salary", "Status")
     val rows = mutableMapOf<String, String>()
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
     <h
     <table>
     <c:forEach items="${rows}" var="list">
     <th>
     <c:out value="${rows.keys}"></c:out><br></th>
     <tr>
         <c:forEach items="${rows.values}" var="listItem">
        <td> ${rows} <br/> </td>
     </c:forEach>
     </tr>
     </c:forEach>
     </table>
 </body>
</table>
</body>
</html>
"""
*
 */