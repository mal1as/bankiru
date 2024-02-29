package com.itmo.blps.bankiru.service

import com.itmo.blps.bankiru.model.CreditRequest
import org.springframework.stereotype.Service

@Service
interface SendMessageService {

    fun sendCreditRequestMessage(creditRequest: CreditRequest)
}