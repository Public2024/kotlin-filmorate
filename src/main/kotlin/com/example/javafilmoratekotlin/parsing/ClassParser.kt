package com.example.javafilmoratekotlin.parsing

import com.example.javafilmoratekotlin.model.Film
import com.example.javafilmoratekotlin.util.TypeSeparator
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.stereotype.Component
import java.lang.reflect.Field
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import kotlin.reflect.full.findAnnotations

@Component
class ClassParser {

    val typeSeparator = TypeSeparator()

    fun extractClassInfo(clazz: Class<*>): ClassView {
        return ClassView(
            simpleName = clazz.simpleName.toString(),
            pkg = clazz.packageName,
            fieldNamesSorted = clazz.declaredFields.map { it.name },
            fields = separateField(clazz),
            description = "schemaAnnotation"
        )
    }

    private fun separateField(clazz: Class<*>): List<FieldView> {

        val fields = mutableListOf<FieldView>()

        val (primitiveFields, objectFields) = clazz.declaredFields.partition { typeSeparator.getPrimitiveTypes(it) }
        val (enumFields, other) = objectFields.partition { it.type.isEnum }
        val (collectionAll, unique) = other.partition { typeSeparator.getCollectionTypes(it) }
        val (collectionPrimitive, collectionUnique) = collectionAll.partition { getPrimitiveCollection(it) }

        primitiveFields.forEach { p -> fields.add(extractField(p, TypeField.PRIMITIVE, clazz)) }
        enumFields.forEach { p -> fields.add(extractField(p, TypeField.ENUM, clazz)) }
        unique.forEach { p -> fields.add(extractField(p, TypeField.UNIQUE, clazz)) }
        collectionPrimitive.forEach { p -> fields.add(extractField(p, TypeField.COLLECTION_PRIMITIVE, clazz)) }
        collectionUnique.forEach { p -> fields.add(extractField(p, TypeField.COLLECTION_UNIQUE, clazz)) }

        return fields
    }

    private fun extractField(field: Field, value: TypeField, clazz: Class<*>): FieldView {
        val annotation = field.annotations.find { it is Schema } as? Schema
        var addInfo: Any? = null
        when (value) {
            TypeField.PRIMITIVE -> addInfo = null
            TypeField.ENUM -> addInfo = extractClassEnum(field.type)
            TypeField.UNIQUE -> addInfo = extractClassInfo(field.type)
            TypeField.COLLECTION_PRIMITIVE -> addInfo = "Type Collection " + field.genericType
            TypeField.COLLECTION_UNIQUE ->
                addInfo =
                    extractClassInfo((field.genericType as ParameterizedType).actualTypeArguments.first() as Class<*>)
        }
        return FieldView(
            name = field.name,
            type = field.type.simpleName,
            description = annotation?.description,
            example = extractAnnotationsScheme(clazz, field.name)?.example,
            required = annotation?.required,
            typeField = value,
            addInfo = addInfo
        )
    }

    private fun extractAnnotationsScheme(clazz: Class<*>, name: String): Schema? {
        val field = clazz.declaredFields.find { it.name.equals(name) }?.annotations?.find { it is Schema } as? Schema
        val fieldConstruct =
            clazz.constructors[0].parameters.find { it.name.equals(name) }?.annotations?.find { it is Schema } as? Schema
        return field ?: fieldConstruct
    }

    private fun extractClassEnum(clazz: Class<*>): MutableList<ClassEnumView> {
        val enum: MutableList<ClassEnumView> = mutableListOf()
        val list = clazz.declaredFields
        for (l in list) {
            enum.add(
                ClassEnumView(
                    value = l.name,
                    description = (l.annotations.find { it is Schema } as? Schema)?.description
                )
            )
        }
        return enum
    }

    private fun getPrimitiveCollection(field: Field): Boolean {
        val getObjectCollection = (field.genericType as ParameterizedType).actualTypeArguments.first() as Class<*>
        return typeSeparator.getPrimitiveTypesClass(getObjectCollection)
    }


    //TODO: посмотреть у Сережи и определять простой объект типа Inststant или сложный класс
    fun Field.isPrimitive() = true

}

//TODO: оличать класс от List<класс> у сережи тоже отличает надо списапать
data class ClassView(
    val simpleName: String,
    val pkg: String,
    val fieldNamesSorted: List<String>,

    //TODO: ENUM d обжекстс или отдлельно?
    val fields: List<FieldView>?,
    val description: String?
)

data class ClassEnumView(
    val value: String,
    val description: String?
)

data class FieldView(
    val name: String,
    val type: String,
    val description: String?,
    val example: String?,
    val required: Boolean?,
    val typeField: TypeField,
    val addInfo: Any?
)

enum class TypeField {
    PRIMITIVE,
    ENUM,
    UNIQUE,
    COLLECTION_PRIMITIVE,
    COLLECTION_UNIQUE
}
