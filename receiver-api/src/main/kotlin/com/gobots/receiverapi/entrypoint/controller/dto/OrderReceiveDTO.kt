package com.gobots.receiverapi.entrypoint.controller.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class OrderReceiveDTO(
    @field:NotBlank @field:Size(min = 1) @get:Schema(example = "213") val id: String,
    @field:NotBlank @get:Schema(example = "order.created") val event: String,
    @field:NotBlank @field:Size(min = 1) @get:Schema(example = "8802123") val orderID: String,
    @field:NotBlank @field:Size(min = 1) @get:Schema(example = "1") val storeID: String,
    @field:NotBlank @field:Size(min = 1) @get:Schema(example = "1771086212") val timestamp: Long,
)