package com.example.doc.controllers.examples

import com.example.doc.model.example.User
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

/**
 * @property UserControllerExample - контроллер пользователей
 *
 */
@RestController
@RequestMapping("/users")
@Tag(name = "Контроллер полтьзователей", description = "API для CRUD пользователей")
class UserControllerExample {

    private final val users = HashMap<Int, User>(4)

    private val id: Int = 0

    /**
     * Функция получения всех пользователей
     */
    @GetMapping
    @Operation(description = "Показать всех пользователей")
    fun returnAllUsers(): ArrayList<User> {
        return ArrayList(users.values)
    }

    /**
     * Функция добавление нового пользователя
     */
    @PostMapping
    @Operation(summary = "Добавить пользователя")
    fun createUser(@Parameter(required = true) @RequestBody user: User, number: Int): User {
        user.id
        users.put(user.id, user)
        return user
    }

    /**
     * Функция изменения пользователя
     */
    @PutMapping
    @Operation(summary = "Обноваить пользователя")
    fun changeUser(@Parameter(required = true) @RequestBody user: User): User {
        if (users.containsKey(user.id))
            users.replace(user.id, user)
        else
            throw Exception("Неверный ID")
        return user;
    }

    /**
     * @suppress
     */
    fun validateUser(user: User) {
        if (user.name == null) {
            user.name = user.login
        }
    }

    /**
     * @suppress
     */
    fun generateId(): Int {
        return id.inc()
    }
}
