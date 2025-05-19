package com.ssu.petsapp.repository

import com.ssu.petsapp.entity.Pet
import org.springframework.data.jpa.repository.JpaRepository

interface PetRepository: JpaRepository<Pet, Long> {}
