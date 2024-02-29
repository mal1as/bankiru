package com.itmo.blps.bankiru.dto.response

data class ErrorResponse(
    val message: String?,
    val status: String?,
    val stackTrace: String?
)
