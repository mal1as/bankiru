package com.itmo.blps.bankiru.utils

import org.eclipse.paho.client.mqttv3.IMqttClient
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class ArtemisMqttClient(
    @Value("\${messaging.artemis.broker_url}") private val brokerUrl: String,
    @Value("\${messaging.artemis.username}") private val username: String,
    @Value("\${messaging.artemis.password}") private val password: String
) {

    private val publisher: IMqttClient

    init {
        publisher = MqttClient(brokerUrl, MqttClient.generateClientId())
        val options = MqttConnectOptions()
        options.isAutomaticReconnect = true
        options.connectionTimeout = 10
        options.userName = username
        options.password = password.toCharArray()
        publisher.connect(options)
    }

    fun publishMessage(message: MqttMessage, topic: String) {
        publisher.publish(topic, message)
    }
}