package com.itmo.blps.bankiru.repository

import com.itmo.blps.bankiru.model.CreditRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface CreditRequestRepository : JpaRepository<CreditRequest, Long> {

    @Query("select cr from CreditRequest cr where cr.requestStatus.id = 1")
    fun findAllPendingRequests(): List<CreditRequest>
}