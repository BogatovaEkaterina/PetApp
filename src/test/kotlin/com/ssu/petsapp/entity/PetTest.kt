package com.ssu.petsapp.entity

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class PetTest{
    @Test
    fun creationTest2() {

        val pet = Pet(
            id = 6789,
            name = "Tom",
            age = 5,
            levelOfHunger = 0.8,
            levelOfThirst = 0.8,
            levelOfPollution = 0.5,
            levelOfFatigue = 0.6,
            mood = 0.8,
            lastTime = LocalDateTime.of(2025, 5, 11, 20, 10, 25, 123456789)
        )

        assertEquals(6789, pet.id)
        assertEquals("Tom", pet.name)
        assertEquals(5, pet.age)
        assertEquals(0.8, pet.levelOfHunger)
        assertEquals(0.8, pet.levelOfThirst)
        assertEquals(0.5, pet.levelOfPollution)
        assertEquals(0.6, pet.levelOfFatigue)
        assertEquals(0.8, pet.mood)
        assertEquals(LocalDateTime.of(2025, 5, 11, 20, 10, 25, 123456789), pet.lastTime)
    }
}