package com.gobots.receiverapi.core.repository

import com.gobots.receiverapi.adapter.infra.repository.entity.OrderEventDocument
import com.gobots.receiverapi.core.model.OrderEvent

interface OrderEventRepository {
    fun findEventByID(eventId: String): Boolean
    fun save(doc: OrderEvent)
    fun findAll(): List<OrderEvent>
    fun findById(id: String): List<OrderEvent>
}