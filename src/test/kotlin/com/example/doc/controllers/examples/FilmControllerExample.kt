package com.example.doc.controllers.examples

import com.example.doc.model.example.Film
import com.example.doc.model.example.User
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import java.time.LocalDate


/**
 * @property FilmControllerExample - контроллер фильмов
 */

@RestController
@RequestMapping()
@Tag(name = "Контроллер фильмов", description = "API для CRUD фильмов")
class FilmControllerExample {

    private val films = HashMap<Int, Film>(4)

    private val id: Int = 0

    /**
     * Функция получения всех фильмов
     */
    @GetMapping("/all")
    @Operation(summary = "Показать все фильмы", description = "Показать все фильмы")
    fun returnAllFilms(): ArrayList<Film> {
        return ArrayList(films.values)
    }

    /**
     * Функция создания нового фильма
     */
    @PostMapping("/post_film")
    @Operation(summary = "Добавить фильм", description = "Добавление фильма в коллекцию")
    @ResponseBody
    fun createFilm(
        @RequestParam(required = true) @RequestBody film: Film,
        id: Int,
        users: Collection<User>,
        values: List<Int>
    ): Film {
        validateFilm(film)
        film.id = generateId()
        films[film.id] = film
        return film
    }

    /**
     * Функция изменения фильма
     */
    @PutMapping
    @Operation(summary = "Изменить фильм")
    fun changeFilm(@RequestParam(required = true) @RequestBody film: Film): Film {
        validateFilm(film)
        if (films.containsKey(film.id))
            films.replace(film.id, film)
        else
            throw Exception("Неверный ID")
        return film
    }

    /**
     * @suppress
     */
    fun validateFilm(film: Film) {
        val trainArrival = LocalDate.of(2018, 12, 31)
        if (film.releaseDate.isBefore(trainArrival))
            throw Exception("Некорретная дата")
    }

    /**
     * @suppress
     */
    fun generateId(): Int {
        return id.inc()
    }
}