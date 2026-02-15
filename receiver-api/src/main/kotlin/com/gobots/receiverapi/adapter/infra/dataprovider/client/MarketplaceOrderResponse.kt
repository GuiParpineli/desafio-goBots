package com.gobots.receiverapi.adapter.infra.dataprovider.client

data class MarketplaceOrderResponse(
    val id: String,
    val storeID: String,
    val productsIDs: List<String>,
    val clientID: String,
    val status: String,
    val createdAt: Long
)