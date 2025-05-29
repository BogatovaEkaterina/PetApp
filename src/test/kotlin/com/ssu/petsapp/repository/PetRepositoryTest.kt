package com.ssu.petsapp.repository

import com.ssu.petsapp.entity.Pet
import com.ssu.petsapp.entity.Users
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import java.time.LocalDateTime

@DataJpaTest
class PetRepositoryTest {

    @Autowired
    private lateinit var testEntityManager: TestEntityManager

    @Autowired
    private lateinit var petRepository: PetRepository

    @org.junit.jupiter.api.Test
    fun `should save and retrieve pet successfully`() {
        // Given
        val user = Users(
            id = 0,
            name = "Test User",
            login = "testuser",
            password = "password"
        )
        val savedUser = testEntityManager.persistAndFlush(user)

        val pet = Pet(
            name = "Fluffy",
            levelOfHunger = 60.0,
            levelOfThirst = 40.0,
            levelOfPollution = 30.0,
            levelOfFatigue = 20.0,
            mood = 90.0,
            lastTime = LocalDateTime.now(),
            user = savedUser
        )

        // When
        val savedPet = petRepository.save(pet)
        testEntityManager.flush()

        // Then
        assertThat(savedPet.id).isNotNull()
        assertThat(savedPet.name).isEqualTo("Fluffy")
        assertThat(savedPet.user.id).isEqualTo(savedUser.id)
    }

    @org.junit.jupiter.api.Test
    fun `should find pets by owner ids`() {
        // Given
        val user1 = Users(id = 0, name = "User1", login = "user1", password = "pass")
        val user2 = Users(id = 0, name = "User2", login = "user2", password = "pass")
        val savedUser1 = testEntityManager.persistAndFlush(user1)
        val savedUser2 = testEntityManager.persistAndFlush(user2)

        val pet1 = Pet(name = "Pet1", user = savedUser1)
        val pet2 = Pet(name = "Pet2", user = savedUser1)
        val pet3 = Pet(name = "Pet3", user = savedUser2)

        testEntityManager.persistAndFlush(pet1)
        testEntityManager.persistAndFlush(pet2)
        testEntityManager.persistAndFlush(pet3)

        // When
        val foundPets = petRepository.findPetsByOwnerIds(listOf(savedUser1.id.toLong()))

        // Then
        assertThat(foundPets).hasSize(2)
        assertThat(foundPets.map { it.name }).containsExactlyInAnyOrder("Pet1", "Pet2")
    }

    @org.junit.jupiter.api.Test
    fun `should return empty list when no pets found for owner ids`() {
        // When
        val foundPets = petRepository.findPetsByOwnerIds(listOf(999L))

        // Then
        assertThat(foundPets).isEmpty()
    }
}
