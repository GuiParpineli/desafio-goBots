
package com.gobots.receiverapi.adapter.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.convert.MongoCustomConversions

@Configuration
class MongoConfig {

    @Bean
    fun mongoCustomConversions(): MongoCustomConversions =
        MongoCustomConversions(
            listOf(
                EventStatusToString(),
                StringToEventStatus(),
            )
        )
}
