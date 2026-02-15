package com.gobots.receiverapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class ReceiverApiApplication

fun main(args: Array<String>) {
    runApplication<ReceiverApiApplication>(*args)
}
