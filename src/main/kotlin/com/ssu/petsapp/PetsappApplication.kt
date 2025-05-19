package com.ssu.petsapp

import com.ssu.petsapp.repository.UsersRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableJpaRepositories
class PetsappApplication

fun main(args: Array<String>) {
	runApplication<PetsappApplication>(*args)
}
