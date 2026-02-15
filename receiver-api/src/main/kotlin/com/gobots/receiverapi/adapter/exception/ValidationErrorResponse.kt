package com.gobots.receiverapi.adapter.exception

data class ValidationErrorResponse(
    val status: Int,
    val message: String,
    val errors: List<FieldError>,
)