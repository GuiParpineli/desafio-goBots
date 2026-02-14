package com.gobots.receiverapi.core.usecase

import com.gobots.receiverapi.core.data.OrderClient
import com.gobots.receiverapi.core.model.OrderEvent
import com.gobots.receiverapi.core.data.repository.OrderEventRepository

class OrderEventUseCaseImpl(
    private val repo: OrderEventRepository,
    private val client: OrderClient,
) : OrderEventUseCase {

    override fun checkEventProcessed(eventId: String) = repo.findEventByID(eventId)

    override fun createEvent(doc: OrderEvent) {
        val snapshot = client.getOrderSnapshot(doc.orderID)
        doc.copy(orderSnapshot = snapshot, processed = true).also { repo.save(it) }
    }

    override fun findAll() = repo.findAll()

    override fun findByID(id: String) = repo.findById(id)
}