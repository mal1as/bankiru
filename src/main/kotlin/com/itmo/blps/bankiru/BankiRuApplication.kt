package com.itmo.blps.bankiru

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.transaction.annotation.EnableTransactionManagement

@SpringBootApplication
@EnableTransactionManagement
@EnableScheduling
class BankiRuApplication

fun main(args: Array<String>) {
    runApplication<BankiRuApplication>(*args)
}