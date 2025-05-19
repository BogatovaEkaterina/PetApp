package com.ssu.petsapp.entity

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class UsersTest {
    @Test
    fun creationTest1() {

        val user = Users(
            id = 12345,
            name = "Katya",
            login = "KatyaLogin",
            password = "KatyaPass"
        )

        assertEquals("Katya", user.name)
        assertEquals("KatyaLogin", user.login)
        assertEquals("KatyaPass", user.password)
        assertEquals(12345, user.id)
    }
}

