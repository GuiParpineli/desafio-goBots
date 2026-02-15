package com.gobots.receiverapi.entrypoint.controller

import com.gobots.receiverapi.adapter.service.OrderEventService
import com.gobots.receiverapi.entrypoint.controller.dto.OrderCompleteResponseDTO
import com.gobots.receiverapi.entrypoint.controller.dto.OrderEventResponseDTO
import com.gobots.receiverapi.entrypoint.controller.dto.OrderReceiveDTO
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.data.mongodb.core.aggregation.MergeOperation.UniqueMergeId.id
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
class OrderEventControllerImpl(
    private val service: OrderEventService
) : OrderEventController {
    private val log = LoggerFactory.getLogger(OrderEventControllerImpl::class.java)

    @PostMapping
    override fun receive(@RequestBody @Valid body: OrderReceiveDTO): ResponseEntity<Void> {
        if (service.checkEventProcessed(body.eventID)) {
            log.info("Duplicate event ignored: {}", body.eventID)
            return ResponseEntity.ok().build()
        }
        service.create(body)
        log.info("Order received: event={}, orderId={}, storeId={}", body.event, body.orderID, body.storeID)

        return ResponseEntity.status(HttpStatus.ACCEPTED).build()
    }

    @GetMapping("/")
    override fun listAll(): List<OrderEventResponseDTO> = service.findAll()

    @GetMapping("/byID/{id}")
    override fun listByID(@PathVariable id: String): List<OrderEventResponseDTO> = service.findByID(id)

    @GetMapping("/complete")
    override fun listAllComplete(): List<OrderCompleteResponseDTO> = service.findAllComplete()
}
