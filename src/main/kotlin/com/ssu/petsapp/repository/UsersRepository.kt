package com.ssu.petsapp.repository

import com.ssu.petsapp.entity.Users
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository

interface UsersRepository: JpaRepository<Users, Int> {
    fun getUsersByName(name: String): MutableList<Users>
}

