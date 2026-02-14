package com.gobots.usecase.order

import com.gobots.model.EventStatus
import com.gobots.model.Order

interface OrderUseCase {
    fun create(storeId: String): Order
    fun updateEvent(orderId: String, status: EventStatus): Order
    fun findAll(): List<Order>
    fun findByStoreId(storeId: String): List<Order>
    fun findById(orderId: String): Order?
}