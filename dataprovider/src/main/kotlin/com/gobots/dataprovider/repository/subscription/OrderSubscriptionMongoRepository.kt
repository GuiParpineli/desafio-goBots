package com.gobots.dataprovider.repository.subscription

import com.gobots.dataprovider.document.OrderSubscriptionDocument
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderSubscriptionMongoRepository : MongoRepository<OrderSubscriptionDocument, String> {
    fun findByStoreIDsContainingAndActiveIsTrue(storeId: String): List<OrderSubscriptionDocument>
}