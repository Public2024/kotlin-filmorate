package org.example.model;

import com.example.javafilmoratekotlin.model.Film;
import com.example.javafilmoratekotlin.model.User;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Objects;


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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public Comments(Long id, String text, User user, Film film) {
        this.id = id;
        this.text = text;
        this.user = user;
        this.film = film;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comments comments = (Comments) o;
        return Objects.equals(id, comments.id) && Objects.equals(text, comments.text) && Objects.equals(user, comments.user) && Objects.equals(film, comments.film);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, user, film);
    }
}
