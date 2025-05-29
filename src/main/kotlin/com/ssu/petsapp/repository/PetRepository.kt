package com.ssu.petsapp.repository

import com.ssu.petsapp.entity.Pet
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface PetRepository : JpaRepository<Pet, Long> {
    @Query("SELECT p FROM Pet p WHERE p.user.id IN :ownerIds")
    fun findPetsByOwnerIds(ownerIds: List<Long>): List<Pet>
}

