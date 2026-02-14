package com.gobots.dataprovider.repository.order

import com.gobots.dataprovider.document.OrderDocument
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderMongoRepository : MongoRepository<OrderDocument, String> {
    fun findByStoreId(storeId: String): List<OrderDocument>
}