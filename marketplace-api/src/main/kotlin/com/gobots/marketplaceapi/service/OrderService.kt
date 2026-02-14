package com.gobots.marketplaceapi.service

import com.gobots.model.EventStatus
import com.gobots.model.Order
import com.gobots.usecase.delivery.OrderDeliveryUseCase
import com.gobots.usecase.order.OrderUseCase
import org.springframework.stereotype.Service

@Service
class OrderService(
    private val orderUseCase: OrderUseCase,
    private val deliveryUseCase: OrderDeliveryUseCase,
) {
    fun create(storeId: String): Order {
        val order = orderUseCase.create(storeId)
        deliveryUseCase.enqueue(
            eventStatus = EventStatus.CREATED,
            orderId = order.id ?: error("order id missing"),
            storeId = order.storeId,
        )
        return order
    }

    fun findAll(): List<Order> = orderUseCase.findAll()

    fun findByStoreId(storeId: String): List<Order> = orderUseCase.findByStoreId(storeId)

    fun updateEvent(orderId: String, status: EventStatus): Order {
        val order = orderUseCase.updateEvent(orderId, status)
        deliveryUseCase.enqueue(
            eventStatus = EventStatus.PAID,
            orderId = order.id ?: error("order id missing"),
            storeId = order.storeId,
        )
        return order
    }
}