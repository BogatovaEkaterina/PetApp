package com.ssu.petsapp.controller


import com.ssu.petsapp.entity.Users
import com.ssu.petsapp.entity.UsersServer
import com.ssu.petsapp.entity.toServer
import com.ssu.petsapp.service.UsersService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UsersController(private val usersService: UsersService) {

    // возвращать разумную ошибку, если пользователь уже существует с таким логином
    @PostMapping("/register")
    fun register(@RequestBody user: UsersServer): UsersServer = usersService.registerUser(user).toServer()

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Long): UsersServer? = usersService.findUserById(id)?.toServer()

    @GetMapping
    fun getAllUsers(): List<UsersServer> = usersService.findAllUsers().map { it.toServer() }

    @GetMapping("/search/{string}")
    fun searchUserByNameOrLogin(@PathVariable string: String): List<UsersServer> =
       usersService.searchUserByNameOrLogin(string).map { it.toServer() }
}


