package com.ssu.petsapp.repository

import com.ssu.petsapp.entity.Users
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import kotlin.test.Test

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private lateinit var testEntityManager: TestEntityManager

    @Autowired
    private lateinit var userRepository: UserRepository

    @Test
    fun `should find user by login`() {
        // Given
        val user = Users(
            id = 0,
            name = "John Doe",
            login = "johndoe",
            password = "password123"
        )
        testEntityManager.persistAndFlush(user)

        // When
        val foundUser = userRepository.findByLogin("johndoe")

        // Then
        assertThat(foundUser).isNotNull
        assertThat(foundUser?.name).isEqualTo("John Doe")
        assertThat(foundUser?.login).isEqualTo("johndoe")
    }

    @Test
    fun `should return null when user not found by login`() {
        // When
        val foundUser = userRepository.findByLogin("nonexistent")

        // Then
        assertThat(foundUser).isNull()
    }

    @Test
    fun `should search users by name and login`() {
        // Given
        val user1 = Users(id = 0, name = "Alice Smith", login = "alice", password = "pass")
        val user2 = Users(id = 0, name = "Bob Johnson", login = "bob", password = "pass")
        val user3 = Users(id = 0, name = "Charlie Brown", login = "charlie_alice", password = "pass")

        testEntityManager.persistAndFlush(user1)
        testEntityManager.persistAndFlush(user2)
        testEntityManager.persistAndFlush(user3)

        // When
        val foundUsers = userRepository.searchUsers("alice")

        // Then
        assertThat(foundUsers).hasSize(2)
        assertThat(foundUsers.map { it.name }).containsExactlyInAnyOrder("Alice Smith", "Charlie Brown")
    }
}
