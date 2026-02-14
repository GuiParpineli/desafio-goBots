package com.gobots.marketplaceapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@ComponentScan(basePackages = ["com.gobots.marketplaceapi", "com.gobots.dataprovider"])
@EnableMongoRepositories(basePackages = ["com.gobots.dataprovider"])
@EnableScheduling
class MarketplaceApiApplication

fun main(args: Array<String>) {
    runApplication<MarketplaceApiApplication>(*args)
}
