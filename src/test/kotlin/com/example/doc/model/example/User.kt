package com.example.doc.model.example

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

/**
 * Дата класс User
 * @constructor конструктор класса
 * @param id идентификатор пользователя
 * @param email почта пользователя
 * @param login логин пользователя
 * @param name имя пользователя
 * @param birthday дата рождения пользователя
 */
@Schema(description = "Информация о пользователе")
data class User(

    @Schema(description = "Идентификатор пользователя")
    var id: Int,

    @Schema(description = "Почта пользователя")
    var email: String,

    @Schema(description = "Логин пользователя")
    var login: String,
    @Schema(description = "Имя пользователя")
    var name: String?,

    @Schema(description = "Дата рождения")
    var birthday: LocalDate
)