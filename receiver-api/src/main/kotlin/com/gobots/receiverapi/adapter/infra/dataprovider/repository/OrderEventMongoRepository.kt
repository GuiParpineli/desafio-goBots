package com.gobots.receiverapi.adapter.infra.dataprovider.repository

import com.gobots.receiverapi.adapter.infra.dataprovider.repository.entity.OrderEventDocument
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderEventMongoRepository : MongoRepository<OrderEventDocument, String> {
    fun existsByEventId(eventId: String): Boolean
    fun findAllByProcessedFalse(): List<OrderEventDocument>
}