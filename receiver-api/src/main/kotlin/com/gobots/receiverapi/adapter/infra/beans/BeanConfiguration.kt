package com.gobots.receiverapi.adapter.infra.beans

import com.gobots.receiverapi.adapter.infra.repository.OrderEventMongoRepository
import com.gobots.receiverapi.core.repository.OrderEventRepository
import com.gobots.receiverapi.core.repository.OrderEventRepositoryImpl
import com.gobots.receiverapi.core.usecase.OrderEventUseCase
import com.gobots.receiverapi.core.usecase.OrderEventUseCaseImpl
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class BeanConfiguration {
    @Bean
    fun upOrderEventRepository(repo: OrderEventMongoRepository) = OrderEventRepositoryImpl(repo)

    @Bean
    fun upOrderUseCase(repo: OrderEventRepository): OrderEventUseCase = OrderEventUseCaseImpl(repo)

}