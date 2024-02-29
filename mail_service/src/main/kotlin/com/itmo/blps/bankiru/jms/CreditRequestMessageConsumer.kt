package com.itmo.blps.bankiru.jms

import com.fasterxml.jackson.databind.ObjectMapper
import com.itmo.blps.bankiru.dto.EmailDetails
import com.itmo.blps.bankiru.service.SendEmailService
import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Service
import javax.jms.Message

@Service
class CreditRequestMessageConsumer(
    private val sendEmailService: SendEmailService
) {

    @JmsListener(destination = "\${messaging.credit_request.approve.queue}")
    fun receiveApproveStatus(message: Message) {
        val jsonTree = ObjectMapper().readTree(message.getBody(ByteArray::class.java))
        sendEmailService.sendEmail(EmailDetails(
            "Уважаемый пользователь! Ваша заявка на кредит №${jsonTree["creditRequestId"].asLong()} в банк ${jsonTree["bankName"].asText()} одобрена.",
            jsonTree["userEmail"].asText(), "Заявка на кредит на сайте banki.ru"
        ))
    }

    @JmsListener(destination = "\${messaging.credit_request.reject.queue}")
    fun receiveRejectStatus(message: Message) {
        val jsonTree = ObjectMapper().readTree(message.getBody(ByteArray::class.java))
        sendEmailService.sendEmail(EmailDetails(
            "Уважаемый пользователь! Ваша заявка на кредит №${jsonTree["creditRequestId"].asLong()} в банк ${jsonTree["bankName"].asText()} отклонена.",
            jsonTree["userEmail"].asText(), "Заявка на кредит на сайте banki.ru"
        ))
    }
}