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
    fun extractClassInfoNew(clazz: Class<*>): ClassViewNew {
        val schemaAnnotation = clazz.annotations.find { it is Schema } as? Schema

        /*поля с аннотацией field*/
        val fields = clazz.declaredFields
            .map { FieldWithAnnotation(field = it, annotation = extractAnnotationsScheme(it)) }
            .filter { it.annotation != null }

        /*fields со schema в конструкторе */
        return ClassViewNew(
            simpleName = clazz.simpleName.toString(),
            pkg = clazz.packageName,
            description = schemaAnnotation?.description,
            fields = getAllFields(fields),
        )
    }

    /*Получение полей data класса*/
    private fun getAllFields(fields: List<FieldWithAnnotation>): List<FieldViewNew> {
        val fieldsView = mutableListOf<FieldViewNew>()
        fields.forEach { fieldsView.add(extractFieldNew(it.field, getTypeField(it.field), it.annotation)) }
        return fieldsView
    }

    /*Парсинг полей*/
    private fun extractFieldNew(field: Field, value: TypeField, annotation: Schema?): FieldViewNew {
        var classOfEnum: List<ClassEnumView>? = null
        var classOfUnique: ClassView? = null

        if (value == TypeField.ENUM) classOfEnum = extractClassEnum(field.type).dropLast(1)
        if (value == TypeField.UNIQUE) classOfUnique = extractClassInfo(field.type)
        if (value == TypeField.COLLECTION_UNIQUE) classOfUnique = extractClassUniqueCollection(field)

        return FieldViewNew(
            name = field.name,
            type = field.type,
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
            else TypeField.COLLECTION_UNIQUE
        } else {
            typeField = TypeField.UNIQUE
        }
        return typeField
    }


    /*Получение объекта уникального класса коллекции*/
    private fun extractClassUniqueCollection(field: Field): ClassView {
        return extractClassInfo((field.genericType as ParameterizedType).actualTypeArguments.first() as Class<*>)
    }







    fun extractClassInfo(clazz: Class<*>): ClassView {
        val schemaAnnotation = clazz.annotations.find { it is Schema } as? Schema
        return ClassView(
            simpleName = clazz.simpleName.toString(),
            pkg = clazz.packageName,
            description = schemaAnnotation?.description,
            fields = separateField(clazz),
        )
    }


    private fun separateField(clazz: Class<*>): List<FieldView> {

        val fields = mutableListOf<FieldView>()

        val (primitiveFields, objectFields) = clazz.declaredFields
            .filter { extractAnnotationsScheme(it) != null }
            .partition { typeSeparator.getPrimitiveTypes(it) }
        val (enumFields, other) = objectFields.partition { it.type.isEnum }
        val (collectionAll, unique) = other.partition { typeSeparator.getCollectionTypes(it) }
        val (collectionPrimitive, collectionUnique) = collectionAll.partition { getPrimitiveCollection(it) }

        primitiveFields.forEach { fields.add(extractField(it, TypeField.PRIMITIVE)) }
        enumFields.forEach { fields.add(extractField(it, TypeField.ENUM)) }
        unique.forEach { fields.add(extractField(it, TypeField.UNIQUE)) }
        collectionPrimitive.forEach { fields.add(extractField(it, TypeField.COLLECTION_PRIMITIVE)) }
        collectionUnique.forEach { fields.add(extractField(it, TypeField.COLLECTION_UNIQUE)) }

        return fields
    }

    private fun extractField(field: Field, value: TypeField): FieldView {
        var classOfEnum: List<ClassEnumView>? = null
        var classOfUnique: ClassView? = null
        var classOfPrimitiveCollection: Type? = null
        var classOfUniqueCollection: ClassView? = null
        when (value) {
            TypeField.PRIMITIVE -> classOfEnum = null
            TypeField.ENUM -> classOfEnum = extractClassEnum(field.type).dropLast(1)
            TypeField.UNIQUE -> classOfUnique = extractClassInfo(field.type)
            TypeField.COLLECTION_PRIMITIVE -> classOfPrimitiveCollection = field.genericType
            TypeField.COLLECTION_UNIQUE -> classOfUniqueCollection =
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

    private fun extractClassEnum(clazz: Class<*>): MutableList<ClassEnumView> {
        val enum: MutableList<ClassEnumView> = mutableListOf()
        val list = clazz.declaredFields
        for (l in list) {
            enum.add(ClassEnumView(value = l.name,
                description = (l.annotations.find { it is Schema } as? Schema)?.description))
        }
        return enum
    }

    private fun getPrimitiveCollection(field: Field): Boolean {
        val getObjectCollection = (field.genericType as ParameterizedType).actualTypeArguments.first() as Class<*>
        return typeSeparator.getPrimitiveTypesClass(getObjectCollection)
    }

}

data class FieldWithAnnotation(
    val field: Field,
    val annotation: Schema?
)

data class ClassView(
    val simpleName: String,
    val pkg: String,
    val description: String?,
    val fields: List<FieldView>,
)

data class ClassEnumView(
    val value: String,
    val description: String?
)

data class ClassViewNew(
    val simpleName: String,
    val pkg: String,
    val description: String?,
    val fields: List<FieldViewNew>,
)

data class FieldViewNew(
    val name: String,
    val type: Class<*>,
    val description: String?,
    val example: String?,
    val required: Boolean?,
    val typeField: TypeField,
    // сложный объект чтобы знать какой енам каким бывает
    val classOfEnum: List<ClassEnumView>?,
    // если объект составной, то тут лежит его описание
    val classOfUnique: ClassView?
)

data class FieldView(
    val name: String,
    val type: String,
    val description: String?,
    val example: String?,
    val required: Boolean?,
    val typeField: TypeField,
    // сложный объект чтобы знать какой енам каким бывает
    val classOfEnum: List<ClassEnumView>?,
    // если объект составной, то тут лежит его описание
    val classOfUnique: ClassView?,


    // TODO: можно ли заменить на val collectionType: Type?, просто как информация, что этот филд не просто classOfUnique, а коллекция из classOfUnique
    val classOfPrimitiveCollection: Type?,
    val classOfUniqueCollection: ClassView?
)

//TODO: переименовать unique в composite и оставить просто COLLECTION
enum class TypeField {
    PRIMITIVE, ENUM, UNIQUE, COLLECTION_PRIMITIVE, COLLECTION_UNIQUE
}

