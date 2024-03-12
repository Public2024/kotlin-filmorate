package org.example.model;

import com.example.javafilmoratekotlin.model.Film;
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

    User user;

    @Schema(description = "Фильм к которому оставлен комментарий")
    Collection<Film> film;

    public Comments(Integer id) {
        this.id = id;
    }

    public Comments(List<String> text) {
        this.text = text;
    }

    public Comments(@Schema(description = "Пользователь который оставил комментарий") User user) {
        this.user = user;
    }

    public Comments(Collection<Film> film) {
        this.film = film;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<String> getText() {
        return text;
    }

    public void setText(List<String> text) {
        this.text = text;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Collection<Film> getFilm() {
        return film;
    }

    public void setFilm(Collection<Film> film) {
        this.film = film;
    }
}
