package com.gobots.receiverapi.adapter.infra.beans

import com.gobots.receiverapi.adapter.infra.dataprovider.repository.OrderEventMongoRepository
import com.gobots.receiverapi.core.data.OrderClient
import com.gobots.receiverapi.core.data.repository.OrderEventRepository
import com.gobots.receiverapi.core.data.repository.OrderEventRepositoryImpl
import com.gobots.receiverapi.core.usecase.OrderEventUseCase
import com.gobots.receiverapi.core.usecase.OrderEventUseCaseImpl
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class BeanConfiguration {
    @Bean
    fun upOrderEventRepository(repo: OrderEventMongoRepository) = OrderEventRepositoryImpl(repo)

    @Bean
    fun upOrderUseCase(repo: OrderEventRepository, client: OrderClient) = OrderEventUseCaseImpl(repo, client)
}