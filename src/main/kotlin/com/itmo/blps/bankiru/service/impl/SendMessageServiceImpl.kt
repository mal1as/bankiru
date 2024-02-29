package com.itmo.blps.bankiru.service.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.itmo.blps.bankiru.model.CreditRequest
import com.itmo.blps.bankiru.service.SendMessageService
import com.itmo.blps.bankiru.utils.ArtemisMqttClient
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class SendMessageServiceImpl(
    @Value("\${messaging.credit_request.approve.queue}") private val approveTopic: String,
    @Value("\${messaging.credit_request.reject.queue}") private val rejectTopic: String,
    private val artemisMqttClient: ArtemisMqttClient
) : SendMessageService {

    override fun sendCreditRequestMessage(creditRequest: CreditRequest) {
        val message = MqttMessage()
        message.payload = getPayloadFromCreditRequest(creditRequest)
        message.qos = 0
        artemisMqttClient.publishMessage(message, if (creditRequest.requestStatus?.id!! == 3L) approveTopic else rejectTopic)
    }

    private fun getPayloadFromCreditRequest(creditRequest: CreditRequest): ByteArray {
        val payloadMap = mapOf<String, Any>(
            Pair("creditRequestId", creditRequest.id!!),
            Pair("bankId", creditRequest.bank?.id!!),
            Pair("bankName", creditRequest.bank.name!!),
            Pair("userEmail", creditRequest.userRequest?.user?.email!!)
        )
        return ObjectMapper().writeValueAsString(payloadMap).toByteArray()
    }
}