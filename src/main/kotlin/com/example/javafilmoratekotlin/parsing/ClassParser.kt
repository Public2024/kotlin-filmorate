package com.example.javafilmoratekotlin.parsing

import com.example.javafilmoratekotlin.util.TypeSeparator
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.stereotype.Component
import java.lang.reflect.Field
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

@Component
class ClassParser() {

    val typeSeparator = TypeSeparator()

    fun extractClassInfo(clazz: Class<*>): ClassView {
        val schemaAnnotation = clazz.annotations.find { it is Schema } as? Schema
        return ClassView(
            simpleName = clazz.simpleName.toString(),
            pkg = clazz.packageName,
            fieldNamesSorted = clazz.declaredFields.map { it.name },
            fields = separateField(clazz),
            methods = MethodParser(this).extractClassInfo(clazz),
            description = schemaAnnotation?.description
        )
    }

    private fun separateField(clazz: Class<*>): MutableMap<String, FieldView> {

        val fields = mutableMapOf<String, FieldView>()

        val (primitiveFields, objectFields) = clazz.declaredFields.filter { extractAnnotationsScheme(it)!=null }
            .partition { typeSeparator.getPrimitiveTypes(it) }
        val (enumFields, other) = objectFields.partition { it.type.isEnum }
        val (collectionAll, unique) = other.partition { typeSeparator.getCollectionTypes(it) }
        val (collectionPrimitive, collectionUnique) = collectionAll.partition { getPrimitiveCollection(it) }

        primitiveFields.forEach { p -> fields[p.name] = extractField(p, TypeField.PRIMITIVE) }
        enumFields.forEach { p -> fields[p.name] = extractField(p, TypeField.ENUM) }
        unique.forEach { p -> fields[p.name] = extractField(p, TypeField.UNIQUE) }
        collectionPrimitive.forEach { p -> fields[p.name] = extractField(p, TypeField.COLLECTION_PRIMITIVE) }
        collectionUnique.forEach { p -> fields[p.name] = extractField(p, TypeField.COLLECTION_UNIQUE) }

        return fields
    }

    private fun extractField(field: Field, value: TypeField): FieldView {
        var classOfEnum: List<ClassEnumView>? = null
        var classOfUnique: ClassView? = null
        var classOfPrimitiveCollection: Type? = null
        var classOfUniqueCollection: ClassView? = null
        when (value) {
            TypeField.PRIMITIVE -> classOfEnum = null
            TypeField.ENUM -> classOfEnum = extractClassEnum(field.type)
            TypeField.UNIQUE -> classOfUnique = extractClassInfo(field.type)
            TypeField.COLLECTION_PRIMITIVE -> classOfPrimitiveCollection = field.genericType
            TypeField.COLLECTION_UNIQUE ->
                classOfUniqueCollection =
                    extractClassInfo((field.genericType as ParameterizedType).actualTypeArguments.first() as Class<*>)
        }
        return FieldView(
            name = field.name,
            type = field.type.simpleName,
            description = extractAnnotationsScheme(field)?.description,
            example = extractAnnotationsScheme(field)?.example,
            required = extractAnnotationsScheme(field)?.required,
            typeField = value,
            classOfEnum = classOfEnum,
            classOfUnique = classOfUnique,
            classOfPrimitiveCollection = classOfPrimitiveCollection,
            classOfUniqueCollection = classOfUniqueCollection
        )
    }

    fun extractAnnotationsScheme(field: Field?): Schema? {
        var annotationSchema = field?.annotations?.find { it is Schema } as? Schema
        if (annotationSchema == null) {
            val valueConstructor = field?.declaringClass?.constructors
            var n = 0
            if (valueConstructor != null) while (n < valueConstructor.size) {
                val value = field.declaringClass?.constructors?.get(n)?.parameters?.find { it.name.equals(field.name) }
                val schema = value?.annotations?.find { it is Schema } as? Schema
                if (schema != null) {
                    annotationSchema = schema
                    break
                }
                n += 1
            }
        }
        return annotationSchema
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
    val fields: MutableMap<String, FieldView>,
    val methods: List<MethodView>?,
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
    val classOfEnum: List<ClassEnumView>?,
    val classOfUnique: ClassView?,
    val classOfPrimitiveCollection: Type?,
    val classOfUniqueCollection: ClassView?
)

enum class TypeField {
    PRIMITIVE,
    ENUM,
    UNIQUE,
    COLLECTION_PRIMITIVE,
    COLLECTION_UNIQUE
}

