package com.itmo.blps.bankiru.jta

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.jta.JtaTransactionManager

@Component
class CustomTransactionManager(
    private val jtaTransactionManager: JtaTransactionManager
) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    fun begin() {
        try {
            logger.info("Transaction started")
            jtaTransactionManager.transactionManager?.begin()
        } catch (e: Exception) {
            logger.warn("Unexpected error while beginning transactions")
            throw Exception()
        }
    }

    fun commit() {
        try {
            jtaTransactionManager.transactionManager?.commit()
            logger.info("Transaction commit success")
        } catch (e: Exception) {
            logger.warn("Unexpected error while committing transactions")

            try {
                jtaTransactionManager.transactionManager?.rollback()
            } catch (ex: Exception) {
                logger.warn("Can't rollback after failed commit")
                throw Exception()

            }

            throw Exception()
        }
    }
}