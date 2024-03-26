package org.example.model;

import com.example.doc.model.example.Genre;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Collection;
import java.util.List;


@Schema(description = "Комментарии к фильму")
public class JavaDataModelTest {

    @Schema(description = "Идентификатор")
    Integer id;

    @Schema(description = "Комментарий")
    List<String> text;

    Genre genre;

    @Schema(description = "Комментарий к жанрам")
    Collection<Genre> genres;

    public JavaDataModelTest(@Schema(description = "Тест жанр") Genre genre) {
        this.genre = genre;
    }
}
