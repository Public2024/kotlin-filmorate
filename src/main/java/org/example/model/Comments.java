package org.example.model;

import com.example.javafilmoratekotlin.model.Film;
import com.example.javafilmoratekotlin.model.Genre;
import com.example.javafilmoratekotlin.model.User;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Collection;
import java.util.List;
import java.util.Objects;


@Schema(description = "Комментарии к фильму")
public class Comments {

    @Schema(description = "Идентификатор")
    Integer id;

    @Schema(description = "Комментарий")
    List<String> text;

    Genre genre;

    @Schema(description = "Комментарий к жанрам")
    Collection<Genre> genres;

    public Comments(@Schema(description = "Тест жанр") Genre genre) {
        this.genre = genre;
    }
}
