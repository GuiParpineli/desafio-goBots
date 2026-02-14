package com.gobots.receiverapi.entrypoint.controller

import com.gobots.receiverapi.adapter.service.OrderEventService
import com.gobots.receiverapi.entrypoint.controller.dto.OrderEventResponseDTO
import com.gobots.receiverapi.entrypoint.controller.dto.OrderReceiveDTO
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/order-receiver")
class OrderEventController(
    private val service: OrderEventService,
) {
    private val log = LoggerFactory.getLogger(OrderEventController::class.java)

    @PostMapping()
    fun receive(@RequestBody body: OrderReceiveDTO): ResponseEntity<Void> {
        if (service.checkEventProcessed(body.id)) {
            log.info("Duplicate event ignored: {}", body.id)
            return ResponseEntity.ok().build()
        }
        service.create(body)
        log.info("Order received: event={}, orderId={}, storeId={}", body.event, body.orderID, body.storeID)

        return ResponseEntity.status(HttpStatus.ACCEPTED).build()
    }

    @GetMapping("/order-receiver/")
    fun listAll(): List<OrderEventResponseDTO> = service.findAll()

    @GetMapping("/order-receiver/byID/{id}")
    fun listAll(@PathVariable id: String): List<OrderEventResponseDTO> = service.findByID(id)
}
