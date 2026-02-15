package com.gobots.marketplaceapi.adapter.configuration.exception

data class FieldError(
    val field: String,
    val rejected: Any?,
    val message: String?,
)