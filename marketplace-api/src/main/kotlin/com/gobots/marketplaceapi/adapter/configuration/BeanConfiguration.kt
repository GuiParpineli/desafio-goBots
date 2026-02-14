package com.gobots.marketplaceapi.adapter.configuration

import com.gobots.repository.OrderDeliveryRepository
import com.gobots.repository.OrderRepository
import com.gobots.repository.OrderSubscriptionRepository
import com.gobots.usecase.delivery.OrderDeliveryUseCaseImpl
import com.gobots.usecase.order.OrderUseCaseImpl
import com.gobots.usecase.subscription.OrderSubscriptionUseCaseImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BeanConfiguration {

    @Bean
    fun orderUseCase(repo: OrderRepository) = OrderUseCaseImpl(repo)

    @Bean
    fun subscriptionUseCase(repo: OrderSubscriptionRepository) = OrderSubscriptionUseCaseImpl(repo)

    @Bean
    fun deliveryUseCase(
        deliveryRepo: OrderDeliveryRepository,
        subscriptionRepo: OrderSubscriptionRepository,
    ) = OrderDeliveryUseCaseImpl(deliveryRepo, subscriptionRepo)
}