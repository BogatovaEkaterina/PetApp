package com.ssu.petsapp.service

import com.ssu.petsapp.entity.Users
import com.ssu.petsapp.entity.UsersServer
import com.ssu.petsapp.repository.UserRepository
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import java.util.*

@ExtendWith(MockKExtension::class)
class UserServiceTest {

    @MockK
    private lateinit var userRepository: UserRepository

    private lateinit var userService: UsersService

    @BeforeEach
    fun setUp() {
        userService = UserServiceImpl(userRepository)
    }

    @Test
    fun `should register user successfully`() {
        // Given
        val userServer = UsersServer(name = "John Doe", login = "johndoe", password = "password")
        val savedUser = Users(id = 1, name = "John Doe", login = "johndoe", password = "password")

        every { userRepository.findByLogin("johndoe") } returns null
        every { userRepository.save(any()) } returns savedUser

        // When
        val result = userService.registerUser(userServer)

        // Then
        assertThat(result.name).isEqualTo("John Doe")
        assertThat(result.login).isEqualTo("johndoe")
        verify { userRepository.findByLogin("johndoe") }
        verify { userRepository.save(any()) }
    }

    @Test
    fun `should find user by id successfully`() {
        // Given
        val userId = 1L
        val user = Users(id = userId.toInt(), name = "John", login = "john", password = "pass")

        every { userRepository.findById(userId) } returns Optional.of(user)

        // When
        val result = userService.findUserById(userId)

        // Then
        assertThat(result?.name).isEqualTo("John")
        verify { userRepository.findById(userId) }
    }

    @Test
    fun `should throw exception when user not found by id`() {
        // Given
        val userId = 999L

        every { userRepository.findById(userId) } returns Optional.empty()

        // When & Then
        val exception = assertThrows<ResponseStatusException> {
            userService.findUserById(userId)
        }
        assertThat(exception.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        verify { userRepository.findById(userId) }
    }

    @Test
    fun `should find user by login successfully`() {
        // Given
        val login = "johndoe"
        val user = Users(id = 1, name = "John", login = login, password = "pass")

        every { userRepository.findByLogin(login) } returns user

        // When
        val result = userService.findUserByLogin(login)

        // Then
        assertThat(result?.login).isEqualTo(login)
        verify { userRepository.findByLogin(login) }
    }

    @Test
    fun `should search users by name or login`() {
        // Given
        val searchTerm = "john"
        val users = listOf(
            Users(id = 1, name = "John Doe", login = "johndoe", password = "pass"),
            Users(id = 2, name = "Jane Johnson", login = "jane", password = "pass")
        )

        every { userRepository.searchUsers(searchTerm) } returns users

        // When
        val result = userService.searchUserByNameOrLogin(searchTerm)

        // Then
        assertThat(result).hasSize(2)
        verify { userRepository.searchUsers(searchTerm) }
    }
}
