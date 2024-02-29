package com.itmo.blps.bankiru.repository

import com.itmo.blps.bankiru.model.Bank
import org.springframework.data.jpa.repository.JpaRepository

interface BankRepository : JpaRepository<Bank, Long>