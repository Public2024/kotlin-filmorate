package org.example.model;

import com.example.javafilmoratekotlin.model.Film;
import com.example.javafilmoratekotlin.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
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
