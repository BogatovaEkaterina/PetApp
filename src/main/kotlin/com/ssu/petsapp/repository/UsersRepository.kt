package com.ssu.petsapp.repository

import com.ssu.petsapp.entity.Users
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository


@Repository
interface UserRepository : JpaRepository<Users, Long> {
    fun findByLogin(login: String): Users?

    @Query("SELECT u FROM Users u WHERE u.name LIKE %:searchTerm% OR u.login LIKE %:searchTerm%")
    fun searchUsers(searchTerm: String): List<Users>
}

