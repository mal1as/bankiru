package com.itmo.blps.bankiru.service

import org.springframework.stereotype.Service

@Service
interface CreditRequestService {

    fun updateCreditRequestStatus(creditRequestId: Long, statusId: Long)

    fun checkPendingRequests()
}