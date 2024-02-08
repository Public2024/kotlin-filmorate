package com.example.javafilmoratekotlin.model

import io.swagger.v3.oas.annotations.media.Schema
import nonapi.io.github.classgraph.json.Id
import java.time.LocalDate
import java.util.*
import javax.validation.constraints.*


/**
 *
 * Дата класс Film
 * @constructor конструктор класса
 * @param id идентификатор фильма
 * @param name наименование фильма
 * @param description описание фильма
 * @param releaseDate дата релиза фильма
 * @param duration продолжительность фильма
 */

@Schema(description = "Информация о фильме")
data class Film(
    //TODO: нужен тест что работает без  field: (надо посикать аннотации не только в полях но и в конструкторах)
    @field:Schema(description = "Идентификатор фильма", required = false)
    //UUII добавить
    var id: Int,
    @field:Schema(description = "Наименование фильма", example = "Пирожок")
    val name: String,
    @field:Schema(description = "Дата релиза фильма")
    val releaseDate: LocalDate,
    @field:Schema(description = "Продолжительность фильма", required = false)
    val duration: Int?,
    @field:Schema(description = "Тэги", required = true)
    val tags: Collection<FilmTag>,
    @field:Schema(description = "Пользователи", required = false)
    val users: Collection<User>,
)

enum class FilmTag {
    @Schema(description = "18+")
    GORE,
    @Schema(description = "comedy")
    COMEDY
}
