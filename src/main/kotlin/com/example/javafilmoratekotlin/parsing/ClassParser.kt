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

    /**
     * Получаем состав полей класса, дополняя мета данными из @Scheme
     *
     * 0. состалвяем список полей класса
     * 1. ищем @Schema надо полями
     * 2. ищем параметры конструкторов с @Schema с такими же именами как поля
     * 3. для каждого поля собираем FieldView
     *
     * // TODO: писать в логи, если поле не наллабл, но required = false и наоборот
     * // TODO: в кейсе когда у нас есть поле х, а аннотация @Scheme висит над конструктором с параметром х (в обычно java классе)
     */

    /*Парсинг data класса*/
    fun extractClassInfo(clazz: Class<*>): ClassView {
        val schemaAnnotation = clazz.annotations.find { it is Schema } as? Schema

        /*поля с аннотацией field*/
        val fields = clazz.declaredFields
            .map { FieldWithAnnotation(field = it, annotation = extractAnnotationsScheme(it)) }
            .filter { it.annotation != null }

        /*fields со schema в конструкторе */
        /*??????*/
        return ClassView(
            simpleName = clazz.simpleName.toString(),
            pkg = clazz.packageName,
            description = schemaAnnotation?.description,
            fields = getAllFields(fields),
        )
    }

    /*Получение полей data класса*/
    private fun getAllFields(fields: List<FieldWithAnnotation>): List<FieldView> {
        val fieldsView = mutableListOf<FieldView>()
        fields.forEach { fieldsView.add(extractField(it.field, getTypeField(it.field), it.annotation)) }
        return fieldsView
    }

    /*Парсинг полей*/
    private fun extractField(field: Field, value: TypeField, annotation: Schema?): FieldView {
        var classOfEnum: List<ClassEnumView>? = null
        var classOfUnique: ClassView? = null

        if (value == TypeField.ENUM) classOfEnum = extractClassEnum(field.type).dropLast(2)
        if (value == TypeField.COMPOSITE) classOfUnique = extractClassInfo(field.type)
        if (value == TypeField.COLLECTION_COMPOSITE) classOfUnique = extractClassCompositeCollection(field)

        return FieldView(
            name = field.name,
            type = field.annotatedType.type,
            description = annotation?.description,
            example = annotation?.example,
            required = annotation?.required,
            typeField = value,
            classOfEnum = classOfEnum,
            classOfUnique = classOfUnique
        )
    }

    /*Получение типа поля*/
    private fun getTypeField(field: Field): TypeField {
        val typeField: TypeField
        if (typeSeparator.getPrimitiveTypes(field)) {
            typeField = TypeField.PRIMITIVE
        } else if (field.type.isEnum) {
            typeField = TypeField.ENUM
        } else if (typeSeparator.getCollectionTypes(field)) {
            typeField = if (typeSeparator.checkingOnPrimitiveCollection(field)) TypeField.COLLECTION_PRIMITIVE
            else TypeField.COLLECTION_COMPOSITE
        } else {
            typeField = TypeField.COMPOSITE
        }
        return typeField
    }

    /*Получение объекта уникального класса коллекции*/
    private fun extractClassCompositeCollection(field: Field): ClassView {
        return extractClassInfo((field.genericType as ParameterizedType).actualTypeArguments.first() as Class<*>)
    }

    //TODO: отдельно искать по всем полям филды с аннотациям, потом отдельно искать по всем консрукторам
    //TODO: собираем все (тип -> FieldView), чтобы понимать, какие типы мы уже распарсили
    private fun extractAnnotationsScheme(field: Field?): Schema? {
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

    /*Парсинг enum класса*/
    private fun extractClassEnum(clazz: Class<*>): MutableList<ClassEnumView> {
        val enum: MutableList<ClassEnumView> = mutableListOf()
        val list = clazz.declaredFields
        for (l in list) {
            enum.add(ClassEnumView(value = l.name,
                description = (l.annotations.find { it is Schema } as? Schema)?.description))
        }
        return enum
    }
}

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
        val typeField: TypeField,
    // сложный объект чтобы знать какой енам каким бывает
        val classOfEnum: List<ClassEnumView>?,
    // если объект составной, то тут лежит его описание
        val classOfUnique: ClassView?
)

//TODO: переименовать unique в composite и оставить просто COLLECTION
enum class TypeField {
    PRIMITIVE, ENUM, COMPOSITE, COLLECTION_PRIMITIVE, COLLECTION_COMPOSITE
}

