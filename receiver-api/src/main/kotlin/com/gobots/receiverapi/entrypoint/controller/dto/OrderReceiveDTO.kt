package com.gobots.receiverapi.entrypoint.controller.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Negative
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size
import org.jetbrains.annotations.NotNull

data class OrderReceiveDTO(
    @field:NotBlank("Campo deve ser enviado")
    @field:Size(min = 1, message = "Campo deve ser maior que 1")
    @get:Schema(example = "213")
    val eventID: String,

    @field:NotBlank("Campo event deve ser enviado")
    @get:Schema(example = "order.created")
    val event: String,

    @field:NotBlank("Campo deve ser enviado")
    @field:Size(min = 1, message = "Campo deve ser maior que 1")
    @get:Schema(example = "8802123")
    val orderID: String,

    @field:NotBlank("Campo deve ser enviado")
    @field:Size(min = 1, message = "Campo deve ser maior que 1")
    @get:Schema(example = "1")
    val storeID: String,

    @field:Positive(message = "Valor do campo deve ser positivo")
    @get:Schema(example = "1771086212")
    val timestamp: Long,
)