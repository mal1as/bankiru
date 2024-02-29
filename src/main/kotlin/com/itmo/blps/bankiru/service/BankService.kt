package com.itmo.blps.bankiru.service

import com.itmo.blps.bankiru.model.Address
import com.itmo.blps.bankiru.model.Bank
import com.itmo.blps.bankiru.model.CreditRule
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
interface BankService {

    fun getAllBanks(pageable: Pageable): Page<Bank>

    fun getBankById(id: Long): Bank

    fun addBank(bank: Bank): Bank

    fun updateBank(bank: Bank): Bank

    fun removeBankById(id: Long)

    fun addCreditRulesToBank(id: Long, creditRules: Set<CreditRule>)

    fun addAddressesToBank(id: Long, addresses: Set<Address>)

    fun removeAddressFromBank(bankId: Long, addressId: Long)
}