package com.ssu.petsapp.controller

import com.ssu.petsapp.entity.Users
import com.ssu.petsapp.service.UsersService
import org.assertj.core.api.Assertions.assertThat
import org.mockito.Mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.assertj.MockMvcTester
import kotlin.test.Test

@WebMvcTest(UsersController::class)
class UsersControllerTest {

    @Autowired
    private lateinit var mockMvcTester: MockMvcTester

    @Mock
    private lateinit var usersService: UsersService

    @Test
    fun `should register user successfully`() {
        // Given
        val userToRegister = Users(id = 0, name = "New User", login = "new_user", password = "password")
        val registeredUser = userToRegister.copy(id = 1)

        // When & Then
        assertThat(mockMvcTester.post().uri("/api/users/register"))
            .hasStatusOk()
            .hasContentType(MediaType.APPLICATION_JSON)
    }

    @Test
    fun `should get user by id when exists`() {
        // Given
        val userId = 1L
        val user = Users(id = 1, name = "Test User", login = "test", password = "password")

        // When & Then
        assertThat(mockMvcTester.get().uri("/api/users/{id}", userId))
            .hasStatusOk()
            .hasContentType(MediaType.APPLICATION_JSON)
    }

    @Test
    fun `should search users successfully`() {
        // Given
        val searchTerm = "test"
        val users = listOf(
            Users(id = 1, name = "Test User", login = "test", password = "password")
        )

        // When & Then
        assertThat(mockMvcTester.get().uri("/api/users/search")
            .param("searchTerm", searchTerm))
            .hasStatusOk()
            .hasContentType(MediaType.APPLICATION_JSON)
    }

    @Test
    fun `should get all users`() {
        // Given
        val users = listOf(
            Users(id = 1, name = "User1", login = "user1", password = "password"),
            Users(id = 2, name = "User2", login = "user2", password = "password")
        )

        // When & Then
        assertThat(mockMvcTester.get().uri("/api/users"))
            .hasStatusOk()
            .hasContentType(MediaType.APPLICATION_JSON)
    }
}