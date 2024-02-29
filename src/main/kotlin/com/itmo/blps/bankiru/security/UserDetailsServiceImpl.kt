package com.itmo.blps.bankiru.security

import com.itmo.blps.bankiru.exception.RequestValidationException
import com.itmo.blps.bankiru.model.User
import com.itmo.blps.bankiru.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(
    private val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(phoneNumber: String?): UserDetails {
        val user: User = userRepository.findUserByPhoneNumber(phoneNumber!!)
            ?: throw RequestValidationException("User not found by phone: $phoneNumber")
        return UserDetailsImpl.build(user)
    }
}