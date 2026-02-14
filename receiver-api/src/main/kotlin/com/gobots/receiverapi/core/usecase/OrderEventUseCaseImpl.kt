package com.gobots.receiverapi.core.usecase

import com.gobots.receiverapi.core.model.OrderEvent
import com.gobots.receiverapi.core.repository.OrderEventRepository

class OrderEventUseCaseImpl(private val repo: OrderEventRepository) : OrderEventUseCase {

    override fun checkEventProcessed(eventId: String): Boolean {
        return repo.findEventByID(eventId)
    }

    override fun createEvent(doc: OrderEvent) {
        repo.save(doc)
    }

    override fun findAll(): List<OrderEvent> {
        return repo.findAll()
    }

    override fun findByID(id: String): List<OrderEvent> {
        return repo.findById(id)
    }
}