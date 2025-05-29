package com.ssu.petsapp.controller

import com.ssu.petsapp.entity.Pet
import com.ssu.petsapp.entity.ServerPet
import com.ssu.petsapp.entity.toServer
import com.ssu.petsapp.service.PetService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/pets")
class PetController(private val petService: PetService) {

    @PostMapping
    fun createPet(@RequestParam ownerId: Long, @RequestBody pet: ServerPet): ServerPet  =
        petService.createPet(ownerId, pet).toServer()

    // Просто для просмотра всех зверушек
    @GetMapping
    fun getAllPets(): List<ServerPet> = petService.getAllPets().map { it.toServer() }

    @GetMapping("/owner/{ownerId}")
    fun getPetsByOwner(@PathVariable ownerId: Long): List<ServerPet> =
        petService.getPetsByOwner(ownerId).map { it.toServer() }

    @PostMapping("/{petId}/feed")
    fun feedPet(@PathVariable petId: Long): ServerPet = petService.feedPet(petId).toServer()

    @PostMapping("/{petId}/water")
    fun waterPet(@PathVariable petId: Long): ServerPet = petService.waterPet(petId).toServer()

    @PostMapping("/{petId}/clean")
    fun cleanPet(@PathVariable petId: Long): ServerPet = petService.cleanPet(petId).toServer()

    @PostMapping("/{petId}/play")
    fun playWithPet(@PathVariable petId: Long): ServerPet = petService.playWithPet(petId).toServer()

    @PostMapping("/{petId}/sleep")
    fun putPetToSleep(@PathVariable petId: Long): ServerPet = petService.putPetToSleep(petId).toServer()
}
