package com.example.javafilmoratekotlin.parsing

import com.example.javafilmoratekotlin.util.TypeSeparator
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.stereotype.Component
import java.lang.reflect.Field
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

@Component
class ClassParser() {
    /*Парсинг data класса*/
    fun extractClassInfo(clazz: Class<*>): ClassView {
        return ClassView(
            simpleName = clazz.simpleName.toString(),
            pkg = clazz.`package`.toString(),
            description = extractScheme(clazz.annotations)?.description,
            fields = extractField(returnAllFieldsWithSchema(clazz)),
        )
    }

    /*Парсинг полей*/
    private fun extractField(fields: List<FieldWithAnnotation>): List<FieldView> {
        return fields.map { fieldWithAnnotation ->
            FieldView(
                name = fieldWithAnnotation.field.name,
                type = fieldWithAnnotation.field.annotatedType.type,
                description = fieldWithAnnotation.annotation?.description,
                example = fieldWithAnnotation.annotation?.example,
                required = fieldWithAnnotation.annotation?.required,
                classOfEnum = extractClassEnum(fieldWithAnnotation),
                classOfComposite = extractOfCompositeClass(fieldWithAnnotation.field)
            )
        }
    }

    private fun extractOfCompositeClass(field: Field): ClassView? {
        return when {
            field.annotatedType.type == field.declaringClass -> null
            field.type.isEnum -> null
            TypeSeparator.isPrimitiveTypes(field) -> null
            TypeSeparator.isCollectionTypes(field) -> extractObjOfCollection(field)
            else -> {
                extractClassInfo(field.type)
            }
        }
    }

    /*Получение объекта класса коллекции*/
    private fun extractObjOfCollection(field: Field): ClassView? {
        val objCollection = (field.genericType as ParameterizedType).actualTypeArguments.first() as Class<*>
        return when {
            TypeSeparator.isPrimitive(objCollection) -> null
            objCollection == field.declaringClass -> null
            else -> {
                extractClassInfo(objCollection)
            }
        }
    }

    /*
    *Получение всех полей с аннотацией Schema*/
    private fun returnAllFieldsWithSchema(clazz: Class<*>): List<FieldWithAnnotation> {
        val fields = clazz.declaredFields.toList()
        val allFieldsWithAnnotation = extractSchemeIntoFields(fields).plus(extractSchemeIntoConstructors(clazz))
        return allFieldsWithAnnotation.map { f ->
            fields.find { it.name == f.name }
                ?.let { FieldWithAnnotation(field = it, annotation = f.annotation) }!!
        }
    }

    /*
    * Поиск полей cо Schema в полях*/
    private fun extractSchemeIntoFields(fields: List<Field>): List<NameWithAnnotation> {
        return fields.map {
            NameWithAnnotation(
                name = it.name,
                annotation = extractScheme(it.annotations)
            )
        }
            .filter { it.annotation != null }
    }

    /*
    * Поиск полей cо Schema в конструкторах*/
    private fun extractSchemeIntoConstructors(clazz: Class<*>): List<NameWithAnnotation> {
        val constructors = clazz.constructors
        val fields = mutableListOf<NameWithAnnotation>()
        constructors.forEach { value ->
            val parameter = value.parameters
            for (p in parameter) {
                val schema = extractScheme(p?.annotations)
                if (schema != null) {
                    fields.add(
                        NameWithAnnotation(
                            name = p.name,
                            annotation = schema
                        )
                    )
                }
            }
        }
        return fields
    }

    /*
    * Поиск Scheme в аннотациях*/
    private fun extractScheme(annotations: Array<Annotation>?): Schema? {
        return annotations?.find { it is Schema } as? Schema
    }

    /*
    *Парсинг enum класса*/
    private fun extractClassEnum(fieldWithAnnotation: FieldWithAnnotation): List<ClassEnumView>? {
        val typeOfClass = fieldWithAnnotation.field.type
        return if (typeOfClass.isEnum)
            typeOfClass.declaredFields.map { field ->
                ClassEnumView(
                    value = field.name,
                    description = (field.annotations.find { it is Schema } as? Schema)?.description)
            }.dropLast(1) /*для удаления из коллеции enum элемента по типу VALUE*/
        else null
    }
}

data class NameWithAnnotation(
    val name: String,
    val annotation: Schema?
)

data class FieldWithAnnotation(
    val field: Field,
    val annotation: Schema?
)

data class ClassEnumView(
    val value: String,
    val description: String?
)

data class ClassView(
    val simpleName: String,
    val pkg: String,
    val description: String?,
    val fields: List<FieldView>,
)

data class FieldView(
    val name: String,
    val type: Type,
    val description: String?,
    val example: String?,
    val required: Boolean?,
    // сложный объект чтобы знать какой енам каким бывает
    val classOfEnum: List<ClassEnumView>? = null,
    // если объект составной, то тут лежит его описание
    val classOfComposite: ClassView? = null
)
