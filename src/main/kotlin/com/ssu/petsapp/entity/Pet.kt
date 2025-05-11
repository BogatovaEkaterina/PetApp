package com.ssu.petsapp.entity

import java.time.LocalDateTime


class Pet(
    val id: Int,
    val name: String,
    val age: Int,
    val levelOfHunger: Double,
    val levelOfThirst: Double,
    val levelOfPollution: Double,
    val levelOfFatigue: Double,
    val mood: Double,
    val lastTime: LocalDateTime
)