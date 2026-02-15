package com.gobots.receiverapi.adapter.infra.dataprovider.client

import com.gobots.receiverapi.adapter.mapper.Mapper.toSnapShot
import com.gobots.receiverapi.core.data.OrderClient
import com.gobots.receiverapi.core.model.OrderSnapshot
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

@Component
class OrderClientImpl(
    @Value($$"${marketplace.api.url}") private val baseUrl: String,
) : OrderClient {

    private val log = LoggerFactory.getLogger(OrderClientImpl::class.java)
    private val client = RestClient.create()

    override fun getOrderSnapshot(orderID: String): OrderSnapshot? {
        return runCatching {
            client.get()
                .uri("$baseUrl/orders/$orderID")
                .retrieve()
                .body<MarketplaceOrderResponse>()?.toSnapShot()
        }.onFailure {
            log.warn("Failed to fetch order {} from marketplace: {}", orderID, it.message)
        }.getOrNull()
    }
}

