package com.gobots.receiverapi.core.model

enum class EventStatus {
    CREATED, PAID, SHIPPED, COMPLETED, CANCELLED;

    fun wireName(): String = when (this) {
        CREATED -> "order.created"
        PAID -> "order.paid"
        SHIPPED -> "order.shipped"
        COMPLETED -> "order.completed"
        CANCELLED -> "order.cancelled"
    }

    companion object {
        fun fromWireName(value: String): EventStatus =
            when (value) {
                "order.created" -> CREATED
                "order.paid" -> PAID
                "order.shipped" -> SHIPPED
                "order.completed" -> COMPLETED
                "order.cancelled" -> CANCELLED
                else -> throw IllegalArgumentException("Unsupported event: $value")
            }
    }
}