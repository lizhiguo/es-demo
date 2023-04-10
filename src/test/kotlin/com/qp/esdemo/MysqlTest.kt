package com.qp.esdemo

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class MysqlTest {
    @Autowired
    private lateinit var personRepository: PersonRepository
    @Test
    fun xx() {
        val person = personRepository.save(Person(name = "张三", age = 20))
        println(person)
    }

}