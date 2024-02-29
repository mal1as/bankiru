package com.itmo.blps.bankiru.security.jaas

import com.itmo.blps.bankiru.exception.RequestValidationException
import com.itmo.blps.bankiru.model.User
import com.itmo.blps.bankiru.repository.UserRepository
import org.springframework.security.authentication.jaas.AuthorityGranter
import java.security.Principal

class AuthorityGranterImpl(
    private val userRepository: UserRepository
) : AuthorityGranter {

    override fun grant(principal: Principal?): MutableSet<String> {
        val user: User = userRepository.findUserByPhoneNumber(principal!!.name)
            ?: throw RequestValidationException("User not found by phone: ${principal.name}")
        return user.roles!!
            .map { it.name!! }.toMutableSet()
    }
}