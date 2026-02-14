package com.gobots.usecase.subscription

import com.gobots.model.EventStatus
import com.gobots.model.OrderSubscription
import com.gobots.repository.OrderSubscriptionRepository

class OrderSubscriptionUseCaseImpl(val repository: OrderSubscriptionRepository) : OrderSubscriptionUseCase {
    override fun subscribe(
        storeIds: List<String>,
        callbackUrl: String
    ): OrderSubscription {
        return repository.save(
            OrderSubscription(
                storeIDs = storeIds,
                callbackUrl = callbackUrl
            )
        )
    }
}