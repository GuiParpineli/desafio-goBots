package com.gobots.usecase.order

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
        val order = repository.findById(orderId)
            ?: throw IllegalArgumentException("Order not found: $orderId")

        require(order.status == EventStatus.CREATED && order.status != status) {
            "Order $orderId cannot be paid in status ${order.status}"
        }

        val paid = order.copy(status = status)
        return repository.save(paid)
    }

    override fun findAll(): List<Order> = repository.findAll()

    override fun findByStoreId(storeId: String): List<Order> = repository.findByStoreId(storeId)
}