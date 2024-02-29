package com.itmo.blps.bankiru.service.impl

import com.itmo.blps.bankiru.exception.RequestValidationException
import com.itmo.blps.bankiru.jta.CustomTransactionManager
import com.itmo.blps.bankiru.model.CreditRequest
import com.itmo.blps.bankiru.model.RequestStatus
import com.itmo.blps.bankiru.model.UserRequest
import com.itmo.blps.bankiru.repository.CreditRuleRepository
import com.itmo.blps.bankiru.repository.RequestStatusRepository
import com.itmo.blps.bankiru.repository.UserRepository
import com.itmo.blps.bankiru.repository.UserRequestRepository
import com.itmo.blps.bankiru.service.UserRequestService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class UserRequestServiceImpl(
    private val transactionManager: CustomTransactionManager,
    private val userRequestRepository: UserRequestRepository,
    private val userRepository: UserRepository,
    private val creditRuleRepository: CreditRuleRepository,
    private val requestStatusRepository: RequestStatusRepository
) : UserRequestService {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    override fun getAllUserRequests(pageable: Pageable): Page<UserRequest> {
        return userRequestRepository.findAll(pageable)
    }

    override fun getUserRequestById(id: Long): UserRequest {
        return userRequestRepository.findById(id).get()
    }


    override fun addUserRequest(userRequest: UserRequest): UserRequest {
        if (userRepository.findById(userRequest.user!!.id!!).get().userData == null) {
            logger.warn("Add user request operation error, user data is null, user id = ${userRequest.user.id}")
            throw RequestValidationException("User must fill information about yourself before create request")
        }

        if(!userRequest.creditRequests.isNullOrEmpty()) {
            userRequest.creditRequests = emptySet()
        }

        transactionManager.begin()
        val savedRequest: UserRequest = userRequestRepository.save(userRequest)

        val pendingStatus: RequestStatus = requestStatusRepository.findById(1).get()
        val createdCreditRequests: Set<CreditRequest> = creditRuleRepository.findAllBySumAndPeriodAndDeposit(
            userRequest.sum!!,
            userRequest.period!!,
            userRequest.deposit!!
        )
            .filter { it.bank != null }
            .map { CreditRequest(null, pendingStatus, it.bank, it, savedRequest) }
            .toSet()

        val newUserRequest = userRequestRepository.save(
            UserRequest(
                savedRequest.id,
                savedRequest.sum,
                savedRequest.period,
                savedRequest.deposit,
                savedRequest.user,
                createdCreditRequests
            )
        )

        transactionManager.commit()
        return newUserRequest
    }

    override fun removeUserRequestById(id: Long) {
        transactionManager.begin()
        userRequestRepository.deleteById(id)
        transactionManager.commit()
    }
}