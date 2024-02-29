package com.itmo.blps.bankiru.config

import com.itmo.blps.bankiru.repository.UserRepository
import com.itmo.blps.bankiru.security.jaas.AuthorityGranterImpl
import com.itmo.blps.bankiru.security.jaas.JaasLoginModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.jaas.AbstractJaasAuthenticationProvider
import org.springframework.security.authentication.jaas.DefaultJaasAuthenticationProvider
import org.springframework.security.authentication.jaas.memory.InMemoryConfiguration
import javax.security.auth.login.AppConfigurationEntry

@Configuration
class JaasConfig(
    private val userRepository: UserRepository
) {

    @Bean
    fun configuration(): InMemoryConfiguration {
        val configurationEntries = arrayOf(AppConfigurationEntry(
            JaasLoginModule::class.java.canonicalName,
            AppConfigurationEntry.LoginModuleControlFlag.REQUIRED, mapOf(Pair("userRepository", userRepository))))
        return InMemoryConfiguration(mapOf(Pair("SPRINGSECURITY", configurationEntries)))
    }

    @Bean
    fun jaasAuthenticationProvider(configuration: javax.security.auth.login.Configuration): AbstractJaasAuthenticationProvider {
        val provider = DefaultJaasAuthenticationProvider()
        provider.setConfiguration(configuration)
        provider.setAuthorityGranters(arrayOf(AuthorityGranterImpl(userRepository)))
        return provider
    }
}