package com.example.javafilmoratekotlin.controllers

import com.example.javafilmoratekotlin.model.Genre
import com.example.javafilmoratekotlin.parsing.*
import com.example.javafilmoratekotlin.service.ApplicationEndpoint
import com.example.javafilmoratekotlin.service.ApplicationEndpointsFinder
import com.example.javafilmoratekotlin.service.DocumentationServiceExample
import com.example.javafilmoratekotlin.view.DocumentationEndpoint
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import org.reflections.Reflections
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.lang.reflect.Type
import kotlin.reflect.javaType
import kotlin.reflect.typeOf


@RestController
@RequestMapping("/doc")
@Tag(name = "DocGenerateController", description = "Контроллер для генерации эндпоинтов приложения")
class DocGenerateController {

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
            type = typeOf<Collection<ActualUser>>().javaType,
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
                        type = String::class.java,
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
            type = Genre::class.java,
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