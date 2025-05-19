package com.ssu.petsapp.entity

import jakarta.persistence.*
import java.time.LocalDateTime


@Entity
@Table(name = "pet")
data class Pet(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val name: String,
    val age: Int,

    @Column(name = "level_of_hunger")
    val levelOfHunger: Double,

    @Column(name = "level_of_thirst")
    val levelOfThirst: Double,

    @Column(name = "level_of_pollution")
    val levelOfPollution: Double,

    @Column(name = "level_of_fatigue")
    val levelOfFatigue: Double,

    val mood: Double,

    @Column(name = "last_time")
    val lastTime: LocalDateTime,

    @ManyToMany(mappedBy = "pets")
    var users: MutableSet<Users> = mutableSetOf()
)