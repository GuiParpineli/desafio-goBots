package com.gobots.marketplaceapi.service

import com.gobots.marketplaceapi.adapter.Mapper.toDTO
import com.gobots.marketplaceapi.entrypoint.dto.OrderCompleteResponseDTO
import com.gobots.marketplaceapi.entrypoint.dto.OrderResponseDTO
import com.gobots.model.EventStatus
import com.gobots.model.Order
import com.gobots.model.OrderResponse
import com.gobots.usecase.delivery.OrderDeliveryUseCase
import com.gobots.usecase.order.OrderUseCase
import org.springframework.stereotype.Service

@Service
class OrderService(
    private val orderUseCase: OrderUseCase,
    private val deliveryUseCase: OrderDeliveryUseCase,
) {
    fun create(storeId: String): OrderResponseDTO {
        val order = orderUseCase.create(storeId)
        deliveryUseCase.enqueue(
            eventStatus = EventStatus.CREATED,
            orderId = order.id ?: error("order id missing"),
            storeId = order.storeId,
        )
        return OrderResponse(order.id!!, order.status.wireName()).toDTO()
    }

    fun findAll(): List<OrderCompleteResponseDTO> = orderUseCase.findAll().map { it.toDTO() }

    fun findByStoreId(storeId: String): List<OrderCompleteResponseDTO> =
        orderUseCase.findByStoreId(storeId).map { it.toDTO() }

    fun updateEvent(orderId: String, status: EventStatus): OrderResponseDTO {
        val order = orderUseCase.updateEvent(orderId, status)
        deliveryUseCase.enqueue(
            eventStatus = EventStatus.PAID,
            orderId = order.id ?: error("order id missing"),
            storeId = order.storeId,
        )
        return OrderResponse(order.id!!, order.status.wireName()).toDTO()
    }

    fun findById(orderId: String): OrderCompleteResponseDTO? {
        return orderUseCase.findById(orderId)?.toDTO()
    }
}