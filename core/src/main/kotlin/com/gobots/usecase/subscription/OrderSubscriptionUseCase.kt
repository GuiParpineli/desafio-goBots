package com.gobots.usecase.subscription

import com.gobots.model.EventStatus
import com.gobots.model.OrderSubscription

interface OrderSubscriptionUseCase {
    fun subscribe(storeIds: List<String>, callbackUrl: String): OrderSubscription
}