package com.example.javafilmoratekotlin.controllers

import com.example.javafilmoratekotlin.model.Film
import com.example.javafilmoratekotlin.model.User
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import javax.validation.Valid


/**
 * @property FilmController - контроллер фильмов
 */
@RestController
@RequestMapping("/films")
@Tag(name = "Контроллер фильмов", description = "API для CRUD фильмов")
class FilmController {

    private val films = HashMap<Int, Film>(4)

    private val id: Int = 0

    /**
     * Функция получения всех фильмов
     */
//    @GetMapping("/all")
//    @Operation(summary = "Показать все фильмы")
//    fun returnAllFilms(): ArrayList<Film> {
//        return ArrayList(films.values)
//    }
//
//    /**
//     * Функция создания нового фильма
//     */
//    @PostMapping
//    @Operation(summary = "Добавить фильм", description = "Добавление фильма в коллекцию")
//    @ResponseBody
//    fun createFilm(@RequestParam(required = true) @Valid  @RequestBody film: Film, id: Int, users: Collection<User>, values: List<Int>): Film {
//        validateFilm(film)
//        film.id = generateId()
//        films[film.id] = film
//        return film
//    }
//
//    /**
//     * Функция изменения фильма
//     */
//    @PutMapping
//    @Operation(summary = "Изменить фильм")
//    fun changeFilm(@RequestParam(required = true) @Valid @RequestBody film: Film): Film {
//        validateFilm(film)
//        if (films.containsKey(film.id))
//            films.replace(film.id, film)
//        else
//            throw Exception("Неверный ID")
//        return film
//    }

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