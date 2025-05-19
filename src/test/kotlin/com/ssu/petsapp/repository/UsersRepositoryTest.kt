package com.ssu.petsapp.repository

import com.ssu.petsapp.entity.Users
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.api.MethodOrderer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.annotation.Rollback

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class UserRepositoryTest {

    @Autowired
    private lateinit var userRepository: UsersRepository

    @Autowired
    private lateinit var entityManager: TestEntityManager

    @Test
    fun testSaveUser() {
        // Given
        val user = Users(
            name = "John Doe",
            login = "johndoe",
            password = "password123"
        )

        // When
        val savedUser = userRepository.save(user)

        // Then
        assertThat(savedUser.id).isNotNull()
        assertThat(savedUser.name).isEqualTo("John Doe")
        assertThat(savedUser.login).isEqualTo("johndoe")
        assertThat(savedUser.password).isEqualTo("password123")
    }

    @Test
    fun testFindUserById() {
        // Given
        val user = Users(name = "Jane Smith", login = "janesmith", password = "pass456")
        entityManager.persist(user)
        entityManager.flush()

        // When
        val foundUser = userRepository.findById(user.id!!)

        // Then
        assertThat(foundUser).isPresent
        assertThat(foundUser.get().name).isEqualTo("Jane Smith")
    }

    @Test
    fun testFindAllUsers() {
        // Given
        entityManager.persist(Users(name = "User 1", login = "user1", password = "pass1"))
        entityManager.persist(Users(name = "User 2", login = "user2", password = "pass2"))
        entityManager.flush()

        // When
        val users = userRepository.findAll()

        // Then
        assertThat(users).isNotEmpty
        assertThat(users.size).isGreaterThanOrEqualTo(2)
    }

    @Test
    fun testUpdateUser() {
        // Given
        val user = Users(name = "Original Name", login = "original", password = "pass")
        entityManager.persist(user)
        entityManager.flush()

        // When
        val userToUpdate = userRepository.findById(user.id!!).get()
        userRepository.save(userToUpdate.copy(name = "Updated name"))

        // Then
        val updatedUser = userRepository.findById(user.id!!).get()
        assertThat(updatedUser.name).isEqualTo("Updated Name")
    }

    @Test
    fun testDeleteUser() {
        // Given
        val user = Users(name = "To Delete", login = "delete", password = "pass")
        entityManager.persist(user)
        entityManager.flush()
        val userId = user.id!!

        // When
        userRepository.deleteById(userId)

        // Then
        val deletedUser = userRepository.findById(userId)
        assertThat(deletedUser).isEmpty
    }
}
