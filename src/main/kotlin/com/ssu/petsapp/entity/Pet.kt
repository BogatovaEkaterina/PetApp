package com.ssu.petsapp.entity

import jakarta.persistence.*
import java.time.LocalDateTime


@Entity
@Table(name = "pets")
data class Pet(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val name: String,
    val levelOfHunger: Double = 50.0, // голод
    val levelOfThirst: Double = 50.0, // жажда
    val levelOfPollution: Double = 50.0, // загрязнение
    val levelOfFatigue: Double = 50.0, // усталость
    val mood: Double = 80.0,
    val lastTime: LocalDateTime = LocalDateTime.now(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: Users
) {
    companion object {
        /**
         * число справа - то, на сколько уменьшается значение за минуту
         * Чтобы подсчитать новое значение = старое - коэф * пройденное_время_в_минутах
         */
        const val HUNGRY_TIME_COEF = 1.0
        const val THIRST_TIME_COEF = 2.0
        const val POLLUTION_TIME_COEF = 0.5
        const val FATIGUE_TIME_COEF = 0.8
        const val MOOD_TIME_COEF = 1.5

        /**
         * число справа - то, на сколько увеличивается значение за действие (кормежка, поилка ... )
         */
        const val HUNGRY_ACTION_COEF = 30.0
        const val THIRST_ACTION_COEF = 20.0
        const val POLLUTION_ACTION_COEF = 50.0
        const val FATIGUE_ACTION_COEF = 40.0
        const val MOOD_ACTION_COEF = 20.0
    }
}


data class ServerPet(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val name: String,
    val levelOfHunger: Double = 50.0, // голод
    val levelOfThirst: Double = 50.0, // жажда
    val levelOfPollution: Double = 50.0, // загрязнение
    val levelOfFatigue: Double = 50.0, // усталость
    val mood: Double = 80.0,
    val lastTime: LocalDateTime = LocalDateTime.now(),
)

fun Pet.toServer() = ServerPet(
    id = id,
    name = name,
    levelOfHunger = levelOfHunger,
    levelOfThirst = levelOfThirst,
    levelOfPollution = levelOfPollution,
    levelOfFatigue = levelOfFatigue,
    mood = mood,
    lastTime = lastTime,
)

fun ServerPet.fromServer(user: Users) = Pet(
    id = id,
    name = name,
    levelOfHunger = levelOfHunger,
    levelOfThirst = levelOfThirst,
    levelOfPollution = levelOfPollution,
    levelOfFatigue = levelOfFatigue,
    mood = mood,
    lastTime = lastTime,
    user = user
)
