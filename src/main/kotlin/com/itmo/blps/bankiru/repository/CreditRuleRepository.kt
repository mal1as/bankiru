package com.itmo.blps.bankiru.repository

import com.itmo.blps.bankiru.model.CreditRule
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface CreditRuleRepository : JpaRepository<CreditRule, Long> {

    @Query("select cr from CreditRule cr where cr.maxSum >= :sum and cr.minSum <= :sum and cr.maxPeriod >= :period and " +
            "cr.minPeriod <= :period and (:deposit = true or cr.creditType.deposit <> true)")
    fun findAllBySumAndPeriodAndDeposit(sum: Long, period: Long, deposit: Boolean): List<CreditRule>
}