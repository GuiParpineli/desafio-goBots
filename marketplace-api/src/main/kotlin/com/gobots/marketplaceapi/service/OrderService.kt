package com.gobots.marketplaceapi.service

import com.gobots.marketplaceapi.adapter.Mapper.toDTO
import com.gobots.exception.NotFoundException
import com.gobots.marketplaceapi.entrypoint.dto.OrderCompleteResponseDTO
import com.gobots.marketplaceapi.entrypoint.dto.OrderResponseDTO
import com.gobots.model.EventStatus
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

    fun findByStoreId(storeId: String): List<OrderCompleteResponseDTO> {
        val findByStoreId = orderUseCase.findByStoreId(storeId).also { if (it.isEmpty()) throw NotFoundException() }
        return findByStoreId.map { it.toDTO() }
    }

    fun updateEvent(orderId: String, status: EventStatus): OrderResponseDTO {
        val order = orderUseCase.updateEvent(orderId, status)

        deliveryUseCase.enqueue(
            eventStatus = status,
            orderId = order.id ?: error("order id missing"),
            storeId = order.storeId,
        )

        return OrderResponse(order.id!!, order.status.wireName()).toDTO()
    }

    fun findById(orderId: String): OrderCompleteResponseDTO {
        orderUseCase.findById(orderId)?.let { return it.toDTO() }
        throw NotFoundException()
    }
}