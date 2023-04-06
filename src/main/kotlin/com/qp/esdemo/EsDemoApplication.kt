package com.qp.esdemo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class EsDemoApplication

fun main(args: Array<String>) {
	runApplication<EsDemoApplication>(*args)
}
