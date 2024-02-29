package com.itmo.blps.bankiru.dto

data class EmailDetails(
    val body: String,
    val receiver: String,
    val subject: String
)