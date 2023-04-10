package com.qp.esdemo

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@Schema(description = "人员信息实体")
@Entity
data class Person(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    @Schema(description = "主键", hidden = true)
    var id: Long? = null,
    var name: String? = null,
    var age: Int = 0) {}
@Repository
interface PersonRepository: JpaRepository<Person, Long>

@RestController
class PersonController(val repository: PersonRepository) {
    @GetMapping("/person")
    fun getPerson(): List<Person> {
        return repository.findAll()
    }
}


