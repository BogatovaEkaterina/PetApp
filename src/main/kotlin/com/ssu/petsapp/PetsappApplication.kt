package com.ssu.petsapp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableJpaRepositories
class PetsappApplication

fun main(args: Array<String>) {
	runApplication<PetsappApplication>(*args)
}
