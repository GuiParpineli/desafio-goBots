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
    @ResponseStatus(HttpStatus.CREATED)
    override fun create(@RequestBody @Valid req: CreateOrderRequest): ResponseEntity<OrderResponseDTO> =
        ResponseEntity.ok(service.create(req.storeId))

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
            ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND, "Order not found: $orderId"
            )
    }

    @GetMapping
    override fun findAll(): ResponseEntity<List<OrderCompleteResponseDTO>> = ResponseEntity.ok(service.findAll())

    @GetMapping(params = ["storeId"])
    override fun findByStore(@RequestParam storeId: String): ResponseEntity<List<OrderCompleteResponseDTO>> =
        ResponseEntity.ok(service.findByStoreId(storeId))

}