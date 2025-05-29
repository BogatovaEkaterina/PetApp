package com.ssu.petsapp.entity

import jakarta.persistence.*

@Entity
@Table(name = "users")
data class Users(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Int,

    val name: String = "",
    val login: String = "",
    val password: String = "",

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val pets: MutableList<Pet> = mutableListOf(),
) {
    override fun toString(): String {
        return toServer().toString()
    }
}


data class UsersServer(
    val id: Int? = null,
    val name: String = "",
    val login: String = "",
    val password: String = "",
    val pets: List<ServerPet> = mutableListOf(),
)

fun Users.toServer() = UsersServer(
    id = id,
    name = name,
    login = login,
    password = password,
    pets = pets.map { it.toServer() }
)

fun UsersServer.fromServer() = Users(
    id = id ?: 0,
    name = name,
    login = login,
    password = password,
    pets = mutableListOf()
)
