package com.gobots.marketplaceapi.entrypoint.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty

data class CreateSubscriptionRequest(
    @field:NotEmpty val storeIDs: List<String>,
    @field:NotBlank val callbackUrl: String
)

data class SubscriptionResponseDTO(val message: String)