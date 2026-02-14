package com.gobots.repository

import com.gobots.model.EventStatus
import com.gobots.model.OrderSubscription

interface OrderSubscriptionRepository {
    fun save(subscription: OrderSubscription): OrderSubscription
    fun findByStoreIdAndEvent(storeId: String): List<OrderSubscription>
    fun findAll(): List<OrderSubscription>
}