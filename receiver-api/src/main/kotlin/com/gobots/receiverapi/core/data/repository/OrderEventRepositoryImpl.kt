package com.gobots.receiverapi.core.data.repository

import com.gobots.receiverapi.adapter.infra.dataprovider.repository.OrderEventMongoRepository
import com.gobots.receiverapi.adapter.mapper.Mapper.toDocument
import com.gobots.receiverapi.adapter.mapper.Mapper.toDomain
import com.gobots.receiverapi.core.model.OrderEvent
import kotlin.jvm.optionals.toList

class OrderEventRepositoryImpl(private val repo: OrderEventMongoRepository) : OrderEventRepository {

    override fun findEventByID(eventId: String): Boolean = repo.existsByEventId(eventId)

    override fun save(doc: OrderEvent) {
        repo.save(doc.toDocument())
    }

    override fun findAll() = repo.findAll().map { it.toDomain() }.toList()

    override fun findById(id: String) = repo.findById(id).map { it.toDomain() }.toList()

    override fun findPendingEvents(): List<OrderEvent> = repo.findAllByProcessedFalse().map { it.toDomain() }
}