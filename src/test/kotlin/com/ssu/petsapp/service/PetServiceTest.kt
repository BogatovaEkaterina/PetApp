
package com.ssu.petsapp.service

import com.ssu.petsapp.entity.Pet
import com.ssu.petsapp.entity.ServerPet
import com.ssu.petsapp.entity.Users
import com.ssu.petsapp.repository.PetRepository
import com.ssu.petsapp.repository.UserRepository
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDateTime
import java.util.*

@ExtendWith(MockKExtension::class)
class PetServiceTest {

    @MockK
    private lateinit var petRepository: PetRepository

    @MockK
    private lateinit var userRepository: UserRepository

    @MockK
    private lateinit var petService: PetService

    @BeforeEach
    fun setUp() {
        petService = PetServiseImpl(petRepository, userRepository)
    }

    @Test
    fun `should create pet successfully`() {
        // Given
        val ownerId = 1L
        val user = Users(id = ownerId.toInt(), name = "Owner", login = "owner", password = "pass")
        val serverPet = ServerPet(name = "Buddy", levelOfHunger = 50.0)
        val savedPet = Pet(id = 1L, name = "Buddy", user = user)

        every { userRepository.findById(ownerId) } returns Optional.of(user)
        every { petRepository.save(any()) } returns savedPet

        // When
        val result = petService.createPet(ownerId, serverPet)

        // Then
        assertThat(result.name).isEqualTo("Buddy")
        assertThat(result.user.id).isEqualTo(ownerId.toInt())
        verify { userRepository.findById(ownerId) }
        verify { petRepository.save(any()) }
    }

    @Test
    fun `should get all pets`() {
        // Given
        val pets = listOf(
            Pet(id = 1L, name = "Pet1", user = mockk()),
            Pet(id = 2L, name = "Pet2", user = mockk())
        )
        every { petRepository.findAll() } returns pets

        // When
        val result = petService.getAllPets()

        // Then
        assertThat(result).hasSize(2)
        assertThat(result.map { it.name }).containsExactlyInAnyOrder("Pet1", "Pet2")
        verify { petRepository.findAll() }
    }

    @Test
    fun `should get pets by owner`() {
        // Given
        val ownerId = 1L
        val pets = listOf(Pet(id = 1L, name = "OwnerPet", user = mockk()))
        every { petRepository.findPetsByOwnerIds(listOf(ownerId)) } returns pets

        // When
        val result = petService.getPetsByOwner(ownerId)

        // Then
        assertThat(result).hasSize(1)
        assertThat(result[0].name).isEqualTo("OwnerPet")
        verify { petRepository.findPetsByOwnerIds(listOf(ownerId)) }
    }

    @Test
    fun `should feed pet and update hunger level`() {
        // Given
        val petId = 1L
        val pet = Pet(
            id = petId,
            name = "Hungry Pet",
            levelOfHunger = 30.0,
            levelOfThirst = 50.0,
            levelOfPollution = 40.0,
            levelOfFatigue = 60.0,
            mood = 70.0,
            lastTime = LocalDateTime.now().minusMinutes(10),
            user = mockk()
        )
        val updatedPet = pet.copy(levelOfHunger = 60.0, lastTime = LocalDateTime.now())

        every { petRepository.findById(petId) } returns Optional.of(pet)
        every { petRepository.save(any()) } returns updatedPet

        // When
        val result = petService.feedPet(petId)

        // Then
        assertThat(result.levelOfHunger).isGreaterThan(pet.levelOfHunger)
        verify { petRepository.findById(petId) }
        verify { petRepository.save(any()) }
    }

    @Test
    fun `should water pet and update thirst level`() {
        // Given
        val petId = 1L
        val pet = Pet(
            id = petId,
            name = "Thirsty Pet",
            levelOfThirst = 20.0,
            lastTime = LocalDateTime.now().minusMinutes(5),
            user = mockk()
        )
        val updatedPet = pet.copy(levelOfThirst = 40.0, lastTime = LocalDateTime.now())

        every { petRepository.findById(petId) } returns Optional.of(pet)
        every { petRepository.save(any()) } returns updatedPet

        // When
        val result = petService.waterPet(petId)

        // Then
        assertThat(result.levelOfThirst).isGreaterThan(pet.levelOfThirst)
        verify { petRepository.findById(petId) }
        verify { petRepository.save(any()) }
    }
}
