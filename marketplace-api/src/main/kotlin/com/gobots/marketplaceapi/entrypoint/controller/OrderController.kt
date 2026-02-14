package com.gobots.marketplaceapi.entrypoint.controller

import com.gobots.marketplaceapi.entrypoint.dto.CreateOrderRequest
import com.gobots.marketplaceapi.entrypoint.dto.OrderResponseDTO
import com.gobots.marketplaceapi.service.OrderService
import com.gobots.model.EventStatus
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/orders")
class OrderController(
    private val service: OrderService,
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody @Valid req: CreateOrderRequest): OrderResponseDTO {
        val order = service.create(req.storeId)
        return OrderResponseDTO(
            id = order.id!!,
            storeId = order.storeId,
            status = order.status.wireName(),
            createdAt = order.createdAt,
        )
    }

    @PostMapping("/{orderId}/{status}")
    fun updateStatus(@PathVariable orderId: String, @PathVariable status: String): OrderResponseDTO {
        val order = service.updateEvent(orderId, EventStatus.fromWireName(status))
        return OrderResponseDTO(
            id = order.id!!,
            storeId = order.storeId,
            status = order.status.wireName(),
            createdAt = order.createdAt,
        )
    }

    @GetMapping
    fun findAll(): List<OrderResponseDTO> =
        service.findAll().map {
            OrderResponseDTO(it.id!!, it.storeId, it.status.wireName(), it.createdAt)
        }

    @GetMapping(params = ["storeId"])
    fun findByStore(@RequestParam storeId: String): List<OrderResponseDTO> =
        service.findByStoreId(storeId).map {
            OrderResponseDTO(it.id!!, it.storeId, it.status.wireName(), it.createdAt)
        }
}