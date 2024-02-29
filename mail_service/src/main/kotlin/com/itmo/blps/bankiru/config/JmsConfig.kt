package com.itmo.blps.bankiru.config

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jms.config.DefaultJmsListenerContainerFactory
import org.springframework.jms.connection.CachingConnectionFactory
import javax.jms.ConnectionFactory

@Configuration
class JmsConfig(
    @Value("\${spring.activemq.user}") private val username: String,
    @Value("\${spring.activemq.password}") private val password: String,
    @Value("\${spring.activemq.broker_url}") private val brokerUrl: String
) {

    @Bean
    fun connectionFactory(): ConnectionFactory {
        val connectionFactory = ActiveMQConnectionFactory()
        connectionFactory.user = username
        connectionFactory.password = password
        connectionFactory.setBrokerURL(brokerUrl)
        return CachingConnectionFactory(connectionFactory)
    }

    @Bean
    fun jmsListenerConnectionFactory(connectionFactory: ConnectionFactory): DefaultJmsListenerContainerFactory {
        val containerFactory = DefaultJmsListenerContainerFactory()
        containerFactory.setConnectionFactory(connectionFactory)
        return containerFactory
    }
}