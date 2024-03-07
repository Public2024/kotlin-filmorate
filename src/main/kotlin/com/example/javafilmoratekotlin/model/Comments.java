package com.example.javafilmoratekotlin.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Комментарии к фильму")
public class Comments {
    @Schema(description = "Идентификатор")
    Long id;

    @Schema(description = "Комментарий")
    String text;

    @Schema(description = "Пользователь который оставил комментарий")
    User user;

    @Schema(description = "Фильм к которому оставлен комментарий")
    Film film;

}
