package com.gobots.dataprovider.repository.delivery

import com.gobots.dataprovider.document.OrderDeliveryDocument
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderDeliveryMongoRepository : MongoRepository<OrderDeliveryDocument, String> {
    fun findByStatusInAndNextAttemptAtLessThanEqual(
        status: Set<String>,
        nextAttemptAt: Long,
        pageable: Pageable,
    ): List<OrderDeliveryDocument>
}