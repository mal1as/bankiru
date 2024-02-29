package com.itmo.blps.bankiru

import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer

class WebInitializer : SpringBootServletInitializer() {

    override fun configure(application: SpringApplicationBuilder?): SpringApplicationBuilder {
        return application!!.sources(BankiRuApplication::class.java)
    }
}