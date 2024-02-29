package com.itmo.blps.bankiru.service.impl

import com.itmo.blps.bankiru.exception.RequestValidationException
import com.itmo.blps.bankiru.jta.CustomTransactionManager
import com.itmo.blps.bankiru.model.Address
import com.itmo.blps.bankiru.model.Bank
import com.itmo.blps.bankiru.model.CreditRule
import com.itmo.blps.bankiru.repository.AddressRepository
import com.itmo.blps.bankiru.repository.BankRepository
import com.itmo.blps.bankiru.repository.CreditRuleRepository
import com.itmo.blps.bankiru.service.BankService
import com.itmo.blps.bankiru.utils.ValidationUtils.Companion.validateEntity
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*

@Service
class BankServiceImpl(
    private val transactionManager: CustomTransactionManager,
    private val bankRepository: BankRepository,
    private val creditRuleRepository: CreditRuleRepository,
    private val addressRepository: AddressRepository
) : BankService {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)


    override fun getAllBanks(pageable: Pageable): Page<Bank> {
        return bankRepository.findAll(pageable)
    }

    override fun getBankById(id: Long): Bank {
        return bankRepository.findById(id).get()
    }

    override fun addBank(bank: Bank): Bank {
        if(bank.id != null) {
            logger.warn("Add bank operation error, received a not null value, bank id must be null")
            throw RequestValidationException("Bank id must be null")
        }
        transactionManager.begin()
        val persistBank = persistBank(bank)
        transactionManager.commit()
        return persistBank

    }

    override fun updateBank(bank: Bank): Bank {
        if (bank.id == null || !bankRepository.findById(bank.id).isPresent) {
            logger.warn("Update bank operation error, bank with id = ${bank.id} not exists")
            throw RequestValidationException("Bank with id = ${bank.id} not exists")
        }
        transactionManager.begin()
        val persistBank = persistBank(bank)
        transactionManager.commit()
        return persistBank
    }

    override fun removeBankById(id: Long) {
        transactionManager.begin()
        bankRepository.deleteById(id)
        transactionManager.commit()
    }


    override fun addCreditRulesToBank(id: Long, creditRules: Set<CreditRule>) {
        creditRules.forEach { validateEntity(it) }
        if(creditRules.any { it.id != null }) {
            logger.warn("Add credit rules to bank operation error, only new credit rules can be added, credit rule ids = ${creditRules.joinToString { it.id.toString() }}")
            throw RequestValidationException("Only new credit rules are available to add")
        }
        val bank: Bank = bankRepository.findById(id).get()
        creditRules.forEach { it.bank = bank }
        transactionManager.begin()
        creditRuleRepository.saveAll(creditRules)
        transactionManager.commit()
    }


    override fun addAddressesToBank(id: Long, addresses: Set<Address>) {
        val bank: Bank = bankRepository.findById(id).get()
        transactionManager.begin()
        bank.addresses = persistAddresses(addresses.plus(if (bank.addresses != null) bank.addresses!! else emptySet()))
        bankRepository.save(bank)
        transactionManager.commit()
    }


    override fun removeAddressFromBank(bankId: Long, addressId: Long) {
        val bankOpt: Optional<Bank> = bankRepository.findById(bankId)
        if(bankOpt.isPresent && bankOpt.get().addresses != null) {
            val bank: Bank = bankOpt.get()
            val newAddresses: Set<Address> = bank.addresses!!.filter { it.id!! != addressId }.toSet()
            bankRepository.save(Bank(bankId, bank.name, bank.description, bank.rate, bank.phoneNumber, bank.ownershipType, bank.creditRules, newAddresses, bank.creditRequests))
        }
    }

    private fun persistAddresses(addresses: Set<Address>?): Set<Address> {
        if (addresses.isNullOrEmpty()) {
            return emptySet()
        }

        return addresses.map {
            if (it.id == null) addressRepository.save(it)
            else addressRepository.findById(it.id).get()
        }.toSet()
    }

    private fun persistBank(bank: Bank): Bank {
        validateEntity(bank)
        bank.addresses = persistAddresses(bank.addresses)
        return bankRepository.save(bank)
    }
}