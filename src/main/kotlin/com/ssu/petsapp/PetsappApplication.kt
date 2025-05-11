package com.ssu.petsapp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PetsappApplication

fun main(args: Array<String>) {
	runApplication<PetsappApplication>(*args)
}
