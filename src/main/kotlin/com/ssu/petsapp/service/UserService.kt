package com.ssu.petsapp.service

import com.ssu.petsapp.repository.UserRepository
import org.springframework.stereotype.Service
import com.ssu.petsapp.entity.Users
import com.ssu.petsapp.entity.UsersServer
import com.ssu.petsapp.entity.fromServer
import kotlin.jvm.optionals.getOrNull
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.server.ResponseStatusException
import org.springframework.http.HttpStatus

interface UsersService {
    fun registerUser(user: UsersServer): Users
    fun findUserById(id: Long): Users?
    fun findUserByLogin(login: String): Users?
    fun findAllUsers(): List<Users>
    fun searchUserByNameOrLogin(login: String): List<Users>
}

@Service
class UserServiceImpl(private val userRepository: UserRepository): UsersService {

    private val logger: Logger = LoggerFactory.getLogger(UserServiceImpl::class.java)

    override fun registerUser(user: UsersServer): Users {
        logger.info("Регистрация пользователя: $user")
        try {
            if (userRepository.findByLogin(user.login) != null) {
                logger.error("Данный логин уже занят")
            }

            val savedUser = userRepository.save(user.fromServer())
            logger.info("Пользователь успешно зарегистрирован: $savedUser")
            return savedUser
        } catch (ex: Exception) {
            logger.error("Ошибка при регистрации пользователя: ${ex.message}", ex)
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Ошибка регистрации пользователя")
        }
    }

    override fun findUserById(id: Long): Users? {
        logger.info("Поиск пользователя по id: $id")
        val user = userRepository.findById(id).getOrNull()
        return if (user != null) {
            logger.info("Пользователь найден: $user")
            user
        } else {
            logger.warn("Пользователь с id=$id не найден")
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден")
        }
    }

    override fun findUserByLogin(login: String): Users? {
        logger.info("Поиск пользователя по логину: $login")
        val user = userRepository.findByLogin(login)
        return if (user != null) {
            logger.info("Пользователь найден: $user")
            user
        } else {
            logger.warn("Пользователь с логином '$login' не найден")
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден")
        }
    }

    override fun findAllUsers(): List<Users> {
        logger.info("Получение списка всех пользователей")
        try {
            val users = userRepository.findAll()
            logger.info("Найдено пользователей: ${users.size}")
            return users
        } catch (ex: Exception) {
            logger.error("Ошибка при получении списка пользователей: ${ex.message}", ex)
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Ошибка получения пользователей")
        }
    }

    override fun searchUserByNameOrLogin(login: String): List<Users> {
        return userRepository.searchUsers(login)
    }
}
