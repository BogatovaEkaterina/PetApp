package com.ssu.petsapp.service

import com.ssu.petsapp.entity.Pet
import com.ssu.petsapp.entity.Pet.Companion.FATIGUE_TIME_COEF
import com.ssu.petsapp.entity.Pet.Companion.HUNGRY_ACTION_COEF
import com.ssu.petsapp.entity.Pet.Companion.HUNGRY_TIME_COEF
import com.ssu.petsapp.entity.Pet.Companion.MOOD_TIME_COEF
import com.ssu.petsapp.entity.Pet.Companion.POLLUTION_TIME_COEF
import com.ssu.petsapp.entity.Pet.Companion.THIRST_TIME_COEF
import com.ssu.petsapp.entity.ServerPet
import com.ssu.petsapp.entity.fromServer
import com.ssu.petsapp.repository.PetRepository
import com.ssu.petsapp.repository.UserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Duration
import java.time.LocalDateTime
import kotlin.jvm.optionals.getOrNull

interface PetService {
    fun createPet(ownerId: Long, pet: ServerPet): Pet
    fun getAllPets(): List<Pet>
    fun getPetsByOwner(ownerId: Long): List<Pet>
    fun feedPet(petId: Long): Pet
    fun waterPet(petId: Long): Pet
    fun cleanPet(petId: Long): Pet
    fun playWithPet(petId: Long): Pet
    fun putPetToSleep(petId: Long): Pet
}

@Service
class PetServiseImpl(
    private val petRepository: PetRepository,
    private val userRepository: UserRepository
): PetService {

    private val logger: Logger = LoggerFactory.getLogger(PetServiseImpl::class.java)

    override fun createPet(ownerId: Long, pet: ServerPet): Pet {
        logger.info("Создание питомца для владельца с id=$ownerId: $pet")
        val user = userRepository.findById(ownerId).get() // TODO: add logging or error handler
        val pet = pet.fromServer(user)
        val savedPet = petRepository.save(pet)
        logger.info("Питомец создан: $savedPet")
        return savedPet
    }

    override fun getAllPets(): List<Pet> {
        logger.info("Получение всех питомцев")
        val pets = petRepository.findAll()
        logger.info("Найдено питомцев: ${pets.size}")
        return pets
    }

    override fun getPetsByOwner(ownerId: Long): List<Pet> {
        logger.info("Получение питомцев для владельца с id=$ownerId")
        val pets = petRepository.findPetsByOwnerIds(listOf(ownerId))
        logger.info("Найдено питомцев у владельца $ownerId: ${pets.size}")
        return pets
    }

    private fun getAndUpdateById(petId: Long, modifier: Pet.() -> Pet): Pet {
        logger.info("Изменение состояния питомца с id=$petId")
        val pet = petRepository.findById(petId).get()
        val now = LocalDateTime.now()
        val minutesPassed = Duration.between(pet.lastTime, now).toSeconds().toDouble() / 60
        logger.debug("Времени прошло с последнего изменения: $minutesPassed минут")

        val updatedPet = pet
            .modifier()
            .applyTimeEffects(minutesPassed)
        logger.info("Состояние питомца после изменений: $updatedPet")
        return petRepository.save(updatedPet)
    }

    private fun Pet.applyTimeEffects(minutes: Double): Pet = copy(
        levelOfHunger = (levelOfHunger - HUNGRY_TIME_COEF * minutes).coerceAtLeast(0.0),
        levelOfThirst = (levelOfThirst - THIRST_TIME_COEF * minutes).coerceAtLeast(0.0),
        levelOfPollution = (levelOfPollution - POLLUTION_TIME_COEF * minutes).coerceAtLeast(0.0),
        levelOfFatigue = (levelOfFatigue - FATIGUE_TIME_COEF * minutes).coerceAtLeast(0.0),
        mood = (mood - MOOD_TIME_COEF * minutes).coerceAtLeast(0.0),
        lastTime = LocalDateTime.now()
    )

    @Transactional
    override fun feedPet(petId: Long): Pet {
        logger.info("Кормление питомца с id=$petId")
        return getAndUpdateById(petId) {
            copy(
                levelOfHunger = (levelOfHunger + HUNGRY_ACTION_COEF).coerceAtMost(100.0),
            )
        }
    }

    @Transactional
    override fun waterPet(petId: Long): Pet {
        logger.info("Поение питомца с id=$petId")
        return getAndUpdateById(petId) {
            copy(
                levelOfThirst = (levelOfThirst + Pet.THIRST_ACTION_COEF).coerceAtMost(100.0),
            )
        }
    }

    @Transactional
    override fun playWithPet(petId: Long): Pet {
        logger.info("Игра с питомцем с id=$petId")
        return getAndUpdateById(petId) {
            copy(
                mood = (mood + Pet.MOOD_ACTION_COEF).coerceAtMost(100.0),
            )
        }
    }

    override fun putPetToSleep(petId: Long): Pet {
        logger.info("Укладывание питомца спать с id=$petId")
        return getAndUpdateById(petId) {
            copy(
                levelOfFatigue = (levelOfFatigue + Pet.FATIGUE_ACTION_COEF).coerceAtMost(100.0),
            )
        }
    }

    @Transactional
    override fun cleanPet(petId: Long): Pet {
        logger.info("Чистка питомца с id=$petId")
        return getAndUpdateById(petId) {
            copy(
                levelOfPollution = (levelOfPollution + Pet.POLLUTION_ACTION_COEF).coerceAtLeast(0.0),
            )
        }
    }
}
