package com.example.javafilmoratekotlin.controllers

import com.example.javafilmoratekotlin.model.Genre
import com.example.javafilmoratekotlin.parsing.*
import com.example.javafilmoratekotlin.service.ApplicationEndpoint
import com.example.javafilmoratekotlin.service.ApplicationEndpointsFinder
import com.example.javafilmoratekotlin.service.DocumentationServiceExample
import com.example.javafilmoratekotlin.view.DocumentViewGeneratorHtml
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import org.reflections.Reflections
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.reflect.javaType
import kotlin.reflect.typeOf


@RestController
@RequestMapping("/doc")
@Tag(name = "DocGenerateController", description = "Контроллер для генерации эндпоинтов приложения")
class DocGenerateController(private val generatorHTML: DocumentViewGeneratorHtml) {

    @GetMapping("/HTML")
    fun generateHTML(): String {
        var headers: List<String> = listOf("ID", "Name", "Salary", "Status")
        var rows = mutableMapOf<String, String>()
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
    }

    @GetMapping
    fun generateDoc(): List<ApplicationEndpoint> {
        val document = DocumentationServiceExample(ApplicationEndpointsFinder(MethodParser(ClassParser())))
        return document.buildDocumentation()
    }

    @OptIn(ExperimentalStdlibApi::class)
    @GetMapping("/field_view")
    fun getClassView(): FieldView {

        @Schema(description = "Информация о пользователе")
        data class ActualUser(
            @Schema(description = "Почта пользователя")
            var email: String,
        )

        data class ActualFilm(
            @field:Schema(description = "Пользователи", required = false)
            val users: Collection<ActualUser>
        )

        return FieldView(
            name = "users",
            type = typeOf<Collection<ActualUser>>().javaType.toString(),
            description = "Пользователи",
            example = "",
            required = false,
            classOfEnum = null,
            classOfComposite = ClassView(
                simpleName = "ActualUser",
                pkg = "package com.example.javafilmoratekotlin.parsing",
                description = "Информация о пользователе",
                fields = listOf(
                    FieldView(
                        name = "email",
                        type = String::class.java.toString(),
                        description = "Почта пользователя",
                        example = "",
                        required = false,
                        classOfEnum = null,
                        classOfComposite = null
                    )
                )
            )
        )
    }


    @GetMapping("/input_parameter")
    fun getInputParameter(): InputParameter {
        return InputParameter(
            name = "SimpleName",
            type = Genre::class.java.toString(),
            required = false,
            classView = null
        )
    }

    /*Ошибка при преобразовании в JSON*/
    @GetMapping("/get_all_data_class")
    fun getAllDataClass(): List<ClassView> {
        val module: String = "com.example"
        val reflections = Reflections(module)

        val dataClasses = reflections.getTypesAnnotatedWith(Schema::class.java)
        return dataClasses.map { ClassParser().extractClassInfo(it) }
    }

    @GetMapping("/get_simple_data_class")
    fun getSimpleDataClass(): ClassView {
        @Schema(description = "Информация о пользователе")
        data class ActualUser(
            @Schema(description = "Почта пользователя")
            var email: String,
        )

        return ClassParser().extractClassInfo(ActualUser::class.java)
    }


}