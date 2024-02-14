package com.example.javafilmoratekotlin.parsing

import com.example.javafilmoratekotlin.util.TypeSeparator
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.stereotype.Component
import java.lang.reflect.Field
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

@Component
class ClassParser {

    val typeSeparator = TypeSeparator()


    fun extractClassInfo(clazz: Class<*>): ClassView {

        val (primitiveFields, objectFields) = clazz.declaredFields.partition { typeSeparator.getPrimitiveTypes(it) }

        val (enumObject, other) = objectFields.partition { it.type.isEnum }

        val (collectionAll, unique) = other.partition { typeSeparator.getCollectionTypes(it) }

        val (collectionPrimitive, collectionUnique) = collectionAll.partition { getPrimitiveCollection(it) }

        return ClassView(
            simpleName = clazz.simpleName.toString(),
            pkg = clazz.packageName,
            fieldNamesSorted = clazz.declaredFields.map { it.name },
            primitives = primitiveFields.map { extractPrimitiveField(it) },
            enum = enumObject.map { extractEnumField(it) },
            unique = unique.map { extractClassInfo(it.type) },
            collectionPrimitive = collectionPrimitive.map { extractPrimitiveCollectionField(it) },
            collectionUnique = collectionUnique.map {extractUniqueCollectionField(it)}
        )
    }

    private fun extractPrimitiveField(field: Field): FieldViewPrimitive {
        val annotation = field.annotations.find { it is Schema } as? Schema
        return FieldViewPrimitive(
            name = field.name,
            type = field.type.simpleName,
            description = annotation?.description,
            example = annotation?.example,
            required = annotation?.required
        )
    }

    private fun extractEnumField(field: Field): FieldViewEnum {
        val annotation = field.annotations.find { it is Schema } as? Schema
        return FieldViewEnum(
            name = field.name,
            type = field.type.simpleName,
            values = extractClassEnum(field.type),
            description = annotation?.description,
            example = annotation?.example,
            required = annotation?.required
        )
    }

    private fun extractPrimitiveCollectionField(field: Field): FieldViewPrimitiveCollection {
        val annotation = field.annotations.find { it is Schema } as? Schema
        return FieldViewPrimitiveCollection(
            name = field.name,
            type = field.type,
            objectCollection = field.genericType,
            description = annotation?.description,
            example = annotation?.example,
            required = annotation?.required
        )
    }

    private fun extractUniqueCollectionField(field: Field): FieldViewUniqueCollection {
        val annotation = field.annotations.find { it is Schema } as? Schema
        val clazz = (field.genericType as ParameterizedType).actualTypeArguments.first() as Class<*>
        return FieldViewUniqueCollection(
            name = field.name,
            type = field.type,
            objectCollection = extractClassInfo(clazz),
            description = annotation?.description,
            example = annotation?.example,
            required = annotation?.required
        )
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
    val primitives: List<FieldViewPrimitive>?,
    val enum: List<FieldViewEnum>?,
    val unique: List<ClassView>,
    val collectionPrimitive: List<FieldViewPrimitiveCollection>,
    val collectionUnique: List<FieldViewUniqueCollection>,
)

data class ClassEnumView(
    val value: String,
    val description: String?
)

data class FieldViewPrimitive(
    val name: String,
    val type: String,
    val description: String?,
    val example: String?,
    val required: Boolean?,
)

data class FieldViewEnum(
    val name: String,
    val type: String,
    val values: MutableList<ClassEnumView>,
    val description: String?,
    val example: String?,
    val required: Boolean?,
)

data class FieldViewPrimitiveCollection(
    val name: String,
    val type: Class<*>,
    val objectCollection: Type,
    val description: String?,
    val example: String?,
    val required: Boolean?,
)

data class FieldViewUniqueCollection(
    val name: String,
    val type: Class<*>,
    val objectCollection: ClassView,
    val description: String?,
    val example: String?,
    val required: Boolean?,
)
