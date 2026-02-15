package com.gobots.dataprovider.configuration

import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@AutoConfiguration
@ComponentScan(basePackages = ["com.gobots.dataprovider"])
@EnableMongoRepositories(basePackages = ["com.gobots.dataprovider"])
class DataproviderAutoConfiguration
