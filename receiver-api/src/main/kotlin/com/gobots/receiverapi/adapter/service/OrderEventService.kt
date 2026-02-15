package com.gobots.receiverapi.adapter.service

import com.gobots.receiverapi.adapter.exception.NotFoundException
import com.gobots.receiverapi.adapter.mapper.Mapper.toCompleteDTO
import com.gobots.receiverapi.adapter.mapper.Mapper.toDTO
import com.gobots.receiverapi.adapter.mapper.Mapper.toDomain
import com.gobots.receiverapi.core.usecase.OrderEventUseCase
import com.gobots.receiverapi.entrypoint.controller.dto.OrderCompleteResponseDTO
import com.gobots.receiverapi.entrypoint.controller.dto.OrderEventResponseDTO
import com.gobots.receiverapi.entrypoint.controller.dto.OrderReceiveDTO
import org.slf4j.LoggerFactory
import org.springframework.data.crossstore.ChangeSetPersister
import org.springframework.data.mongodb.core.aggregation.MergeOperation.UniqueMergeId.id
import org.springframework.stereotype.Service

@Service
class OrderEventService(private val useCase: OrderEventUseCase) {
    private val log = LoggerFactory.getLogger(OrderEventService::class.java)

    fun checkEventProcessed(eventId: String): Boolean {
        log.info("Checking event processed: {}", eventId)
        try {
            return useCase.checkEventProcessed(eventId)
        } catch (e: Exception) {
            log.error("Error checking event processed: {}", eventId, e)
            return false
        }
    }

    fun create(doc: OrderReceiveDTO) {
        log.info("Creating order event: {}", doc.orderID)
        useCase.createEvent(doc.toDomain())
    }

    fun findAll(): List<OrderEventResponseDTO> {
        return useCase.findAll().also { if (it.isEmpty()) throw NotFoundException() }
            .map { it.toDTO() }.toList()
    }

    fun findByID(id: String): List<OrderEventResponseDTO> {
        return useCase.findByID(id).also { if (it.isEmpty()) throw NotFoundException() }
            .map { it.toDTO() }.toList()
    }

    fun findAllComplete(): List<OrderCompleteResponseDTO> {
        return useCase.findAll().also { if (it.isEmpty()) throw NotFoundException() }
            .map { it.toCompleteDTO() }.toList()
    }
}
