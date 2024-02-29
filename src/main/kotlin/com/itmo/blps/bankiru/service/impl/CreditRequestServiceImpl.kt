package com.itmo.blps.bankiru.service.impl

import com.itmo.blps.bankiru.jta.CustomTransactionManager
import com.itmo.blps.bankiru.model.CreditRequest
import com.itmo.blps.bankiru.model.RequestStatus
import com.itmo.blps.bankiru.repository.CreditRequestRepository
import com.itmo.blps.bankiru.repository.RequestStatusRepository
import com.itmo.blps.bankiru.service.CreditRequestService
import com.itmo.blps.bankiru.service.SendMessageService
import com.itmo.blps.bankiru.utils.DadataIntegrationUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class CreditRequestServiceImpl(
    @Value("\${credit_request.min_rate}") private val minRate: Double,
    private val transactionManager: CustomTransactionManager,
    private val creditRequestRepository: CreditRequestRepository,
    private val requestStatusRepository: RequestStatusRepository,
    private val sendMessageService: SendMessageService,
    private val dadataIntegrationUtils: DadataIntegrationUtils
) : CreditRequestService {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    override fun updateCreditRequestStatus(creditRequestId: Long, statusId: Long) {
        val oldCreditRequest: CreditRequest = creditRequestRepository.findById(creditRequestId).get()
        val newRequestStatus: RequestStatus = requestStatusRepository.findById(statusId).get()

        transactionManager.begin()
        val savedCreditRequest: CreditRequest = creditRequestRepository.save(
            CreditRequest(
                creditRequestId,
                newRequestStatus,
                oldCreditRequest.bank,
                oldCreditRequest.creditRule,
                oldCreditRequest.userRequest
            )
        )

        if(statusId > 1) {
            sendMessageService.sendCreditRequestMessage(savedCreditRequest)
        }
        transactionManager.commit()
    }

    @Scheduled(cron = "0 0 * * * *")
    override fun checkPendingRequests() {
        creditRequestRepository.findAllPendingRequests().forEach {request ->
            val profitPerMonth = request.userRequest?.user?.userData?.profitPerMonth
            val workCompany = request.userRequest?.user?.userData?.workCompany
            val creditSum = request.userRequest?.sum!!
            val newStatusId = if (profitPerMonth != null && workCompany != null && creditSum * minRate <= profitPerMonth
                && dadataIntegrationUtils.checkExistCompany(workCompany)) 3L else 2L
            updateCreditRequestStatus(request.id!!, newStatusId)
            logger.info("CreditRequest with id = ${request.id} was processed, new status id = $newStatusId")
        }
    }
}