package com.gobots.receiverapi.core.usecase

import com.gobots.receiverapi.core.model.OrderEvent

interface OrderEventUseCase {
    fun checkEventProcessed(eventId: String): Boolean
    fun createEvent(doc: OrderEvent)
    fun findAll(): List<OrderEvent>
    fun findByID(id: String): List<OrderEvent>
}