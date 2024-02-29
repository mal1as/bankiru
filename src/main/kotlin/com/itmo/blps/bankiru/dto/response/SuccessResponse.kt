package com.itmo.blps.bankiru.dto.response

data class SuccessResponse<T>(
    val success: Boolean = true,
    val content: T? = null,
    val totalPages: Int = 1,
    val totalCount: Long = 1
)