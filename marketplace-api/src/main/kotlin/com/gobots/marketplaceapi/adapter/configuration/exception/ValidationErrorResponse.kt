package com.gobots.marketplaceapi.adapter.configuration.exception

data class ValidationErrorResponse(
    val status: Int,
    val message: String,
    val errors: List<FieldError>,
)