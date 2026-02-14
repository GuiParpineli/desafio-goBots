package com.gobots.marketplaceapi.entrypoint.controller

import com.gobots.marketplaceapi.entrypoint.dto.CreateOrderRequest
import com.gobots.marketplaceapi.entrypoint.dto.OrderCompleteResponseDTO
import com.gobots.marketplaceapi.entrypoint.dto.OrderResponseDTO
import com.gobots.marketplaceapi.service.OrderService
import com.gobots.model.EventStatus
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/orders")
class OrderController(
    private val service: OrderService,
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody @Valid req: CreateOrderRequest): OrderResponseDTO {
        return service.create(req.storeId)
    }

    @PostMapping("/{orderId}/{status}")
    fun updateStatus(@PathVariable orderId: String, @PathVariable status: String): OrderResponseDTO {
        return service.updateEvent(orderId, EventStatus.fromWireName(status))
    }

    @GetMapping("/{orderId}")
    fun findById(@PathVariable orderId: String): OrderCompleteResponseDTO {
        return service.findById(orderId)
            ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND, "Order not found: $orderId"
            )
    }

    @GetMapping
    fun findAll(): List<OrderCompleteResponseDTO> = service.findAll()

    @GetMapping(params = ["storeId"])
    fun findByStore(@RequestParam storeId: String): List<OrderCompleteResponseDTO> = service.findByStoreId(storeId)

}