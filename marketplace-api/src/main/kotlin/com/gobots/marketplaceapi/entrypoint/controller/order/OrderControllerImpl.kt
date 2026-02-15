package com.gobots.marketplaceapi.entrypoint.controller.order

import com.gobots.marketplaceapi.entrypoint.dto.CreateOrderRequest
import com.gobots.marketplaceapi.entrypoint.dto.OrderCompleteResponseDTO
import com.gobots.marketplaceapi.entrypoint.dto.OrderResponseDTO
import com.gobots.marketplaceapi.service.OrderService
import com.gobots.model.EventStatus
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/orders")
class OrderControllerImpl(private val service: OrderService) : OrderController {

    @PostMapping
    override fun create(@RequestBody @Valid req: CreateOrderRequest): ResponseEntity<OrderResponseDTO> {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(req.storeId))
    }

    @PatchMapping("/{orderId}/{status}")
    override fun updateStatus(
        @PathVariable orderId: String,
        @PathVariable status: String
    ): ResponseEntity<OrderResponseDTO> {
        return ResponseEntity.accepted().body(service.updateEvent(orderId, EventStatus.fromWireName(status)))
    }

    @GetMapping("/{orderId}")
    override fun findById(@PathVariable orderId: String): ResponseEntity<OrderCompleteResponseDTO> {
        return ResponseEntity.ok(service.findById(orderId))
    }

    @GetMapping
    override fun findAll(): ResponseEntity<List<OrderCompleteResponseDTO>> = ResponseEntity.ok(service.findAll())

    @GetMapping("{storeId}")
    override fun findByStore(@PathVariable storeId: String): ResponseEntity<List<OrderCompleteResponseDTO>> {
        return ResponseEntity.ok(service.findByStoreId(storeId))
    }
}