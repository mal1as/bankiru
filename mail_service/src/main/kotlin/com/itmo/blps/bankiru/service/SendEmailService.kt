package com.itmo.blps.bankiru.service

import com.itmo.blps.bankiru.dto.EmailDetails
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.MailException
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class SendEmailService(
    @Value("\${spring.mail.username}") private val username: String,
    private val emailSender: JavaMailSender
) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    fun sendEmail(emailDetails: EmailDetails) {
        try {
            val mailMessage = SimpleMailMessage()
            mailMessage.setFrom(username)
            mailMessage.setTo(emailDetails.receiver)
            mailMessage.setText(emailDetails.body)
            mailMessage.setSubject(emailDetails.subject)
            emailSender.send(mailMessage)
        } catch (e: MailException) {
            logger.error("Error while sending email to ${emailDetails.receiver}")
        }
    }
}