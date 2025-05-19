package com.ssu.petsapp.entity

import jakarta.persistence.*
import lombok.NoArgsConstructor

@Entity
@Table(name="users")
data class Users(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Int = 0,

    val name: String = "",
    val login: String = "",
    val password: String = "",

    @ManyToMany
    @JoinTable(
        name = "user_pet",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "pet_id")]
    )
    val pets: MutableSet<Pet> = mutableSetOf()
)