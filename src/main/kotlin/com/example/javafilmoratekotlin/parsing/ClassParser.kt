package com.example.javafilmoratekotlin.parsing

import com.example.javafilmoratekotlin.util.TypeSeparator
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.stereotype.Component
import java.lang.reflect.Field
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

@Component
class ClassParser(private val typeSeparator: TypeSeparator) {


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
        return fields.map { extractField(it) }
    }

    /*Парсинг полей*/
    private fun extractField(fieldWithAnnotation: FieldWithAnnotation): FieldView {

        var classOfEnum: List<ClassEnumView>? = null
        var classOfComposite: ClassView? = null

        val typeField = getTypeField(fieldWithAnnotation.field)

        if (typeField == TypeField.ENUM) classOfEnum = extractClassEnum(fieldWithAnnotation.field.type).dropLast(2)
        if (typeField == TypeField.COMPOSITE) classOfComposite = extractClassInfo(fieldWithAnnotation.field.type)
        if (typeField == TypeField.COLLECTION_COMPOSITE) classOfComposite =
            extractClassCompositeCollection(fieldWithAnnotation.field)

        return FieldView(
            name = fieldWithAnnotation.field.name,
            type = fieldWithAnnotation.field.annotatedType.type,
            description = fieldWithAnnotation.annotation?.description,
            example = fieldWithAnnotation.annotation?.example,
            required = fieldWithAnnotation.annotation?.required,
            typeField = typeField,
            classOfEnum = classOfEnum,
            classOfUnique = classOfComposite
        )
    }

    /*Получение типа поля*/
    private fun getTypeField(field: Field): TypeField {
        return when {
            typeSeparator.getPrimitiveTypes(field) -> TypeField.PRIMITIVE
            field.type.isEnum -> TypeField.ENUM
            typeSeparator.getCollectionTypes(field) -> {
                if (typeSeparator.checkingOnPrimitiveCollection(field)) TypeField.COLLECTION_PRIMITIVE
                else TypeField.COLLECTION_COMPOSITE
            }

            else -> TypeField.COMPOSITE
        }
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
    private fun extractClassEnum(clazz: Class<*>): List<ClassEnumView> {
        return clazz.declaredFields.map { field ->
            ClassEnumView(
                value = field.name,
                description = (field.annotations.find { it is Schema } as? Schema)?.description)
        }
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
    //TODO: возможно java класс Type не нужен и досточноно схрать имя-пакейт класса, чтобы значть что это такое
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

