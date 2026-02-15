package com.gobots.receiverapi.adapter.exception

data class FieldError(
    val field: String,
    val rejected: Any?,
    val message: String?,
)