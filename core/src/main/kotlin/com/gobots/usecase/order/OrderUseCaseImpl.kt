package com.gobots.usecase.order

import com.gobots.exception.NotFoundException
import com.gobots.model.EventStatus
import com.gobots.model.Order
import com.gobots.repository.OrderRepository

class OrderUseCaseImpl(
    private val repository: OrderRepository,
) : OrderUseCase {

    override fun create(storeId: String): Order {
        val order = Order(storeId = storeId, status = EventStatus.CREATED)
        return repository.save(order)
    }

    override fun updateEvent(orderId: String, status: EventStatus): Order {
        val order = repository.findById(orderId) ?: throw NotFoundException()
        order.status.transitionTo(status)
        val modifiedOrder = order.copy(status = status)
        return repository.save(modifiedOrder)
    }

    override fun findAll(): List<Order> = repository.findAll()

    override fun findByStoreId(storeId: String): List<Order> = repository.findByStoreId(storeId)
    override fun findById(orderId: String) = repository.findById(orderId)
}