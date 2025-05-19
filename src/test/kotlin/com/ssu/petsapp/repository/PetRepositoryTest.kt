package com.ssu.petsapp.repository

import com.ssu.petsapp.entity.Pet
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.api.MethodOrderer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.annotation.Rollback
import java.time.LocalDateTime

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class PetRepositoryTest {

    @Autowired
    private lateinit var petRepository: PetRepository

    @Autowired
    private lateinit var entityManager: TestEntityManager

    @Test
    @Order(1)
    @Rollback(false)
    fun testSavePet() {
        // Given
        val pet = Pet(
            name = "Fluffy",
            age = 3,
            levelOfHunger = 0.5,
            levelOfThirst = 0.4,
            levelOfPollution = 0.2,
            levelOfFatigue = 0.3,
            mood = 1.0,
            lastTime = LocalDateTime.now()
        )

        // When
        val savedPet = petRepository.save(pet)

        // Then
        assertThat(savedPet.id).isNotNull()
        assertThat(savedPet.name).isEqualTo("Fluffy")
        assertThat(savedPet.age).isEqualTo(3)
        assertThat(savedPet.mood).isEqualTo(1.0)
    }

    @Test
    @Order(2)
    fun testFindPetById() {
        // Given
        val now = LocalDateTime.now()
        val pet = Pet(
            name = "Rex",
            age = 5,
            levelOfHunger = 3.0,
            levelOfThirst = 2.0,
            levelOfPollution = 1.0,
            levelOfFatigue = 4.0,
            mood = 1.0,
            lastTime = now
        )
        entityManager.persist(pet)
        entityManager.flush()

        // When
        val foundPet = petRepository.findById(pet.id!!)

        // Then
        assertThat(foundPet).isPresent
        assertThat(foundPet.get().name).isEqualTo("Rex")
        assertThat(foundPet.get().age).isEqualTo(5)
        assertThat(foundPet.get().lastTime).isEqualToIgnoringNanos(now)
    }

    @Test
    @Order(3)
    fun testFindAllPets() {
        // Given
        val now = LocalDateTime.now()
        entityManager.persist(
            Pet(
                name = "Pet 1",
                age = 1,
                levelOfHunger = 1.0,
                levelOfThirst = 1.0,
                levelOfPollution = 0.1,
                levelOfFatigue = 0.1,
                mood = 0.6,
                lastTime = now
            )
        )
        entityManager.persist(
            Pet(
                name = "Pet 2",
                age = 2,
                levelOfHunger = 0.2,
                levelOfThirst = 0.2,
                levelOfPollution = 0.2,
                levelOfFatigue = 0.2,
                mood = 0.55,
                lastTime = now
            )
        )
        entityManager.flush()

        // When
        val pets = petRepository.findAll()

        // Then
        assertThat(pets).isNotEmpty
        assertThat(pets.size).isGreaterThanOrEqualTo(2)
    }

    @Test
    @Order(4)
    @Rollback(false)
    fun testUpdatePet() {
        // Given
        val pet = Pet(
            name = "Original",
            age = 1,
            levelOfHunger = 1.0,
            levelOfThirst = 1.0,
            levelOfPollution = 1.0,
            levelOfFatigue = 1.0,
            mood = 0.7,
            lastTime = LocalDateTime.now()
        )
        entityManager.persist(pet)
        entityManager.flush()

        // When
        val petToUpdate = petRepository.findById(pet.id!!).get()
        petRepository.save(petToUpdate.copy(
            name = "Updated",
            mood = 1.0
        ))

        // Then
        val updatedPet = petRepository.findById(pet.id!!).get()
        assertThat(updatedPet.name).isEqualTo("Updated")
        assertThat(updatedPet.mood).isEqualTo(1.0)
    }

    @Test
    @Order(5)
    @Rollback(false)
    fun testDeletePet() {
        // Given
        val pet = Pet(
            name = "To Delete",
            age = 1,
            levelOfHunger = 0.1,
            levelOfThirst = 0.1,
            levelOfPollution = 0.1,
            levelOfFatigue = 0.1,
            mood = 0.05,
            lastTime = LocalDateTime.now()
        )
        entityManager.persist(pet)
        entityManager.flush()
        val petId = pet.id!!

        // When
        petRepository.deleteById(petId)

        // Then
        val deletedPet = petRepository.findById(petId)
        assertThat(deletedPet).isEmpty
    }
}
