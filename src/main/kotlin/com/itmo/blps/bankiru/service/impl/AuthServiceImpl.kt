package com.itmo.blps.bankiru.service.impl

import com.itmo.blps.bankiru.exception.RequestValidationException
import com.itmo.blps.bankiru.exception.SecurityException
import com.itmo.blps.bankiru.jta.CustomTransactionManager
import com.itmo.blps.bankiru.model.Role
import com.itmo.blps.bankiru.model.User
import com.itmo.blps.bankiru.repository.RoleRepository
import com.itmo.blps.bankiru.repository.UserRepository
import com.itmo.blps.bankiru.service.AuthService
import com.itmo.blps.bankiru.utils.ValidationUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthServiceImpl(
    private val transactionManager: CustomTransactionManager,
    private val roleRepository: RoleRepository,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val authenticationManager: AuthenticationManager
) : AuthService {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    override fun authUser(user: User): User {
        val authentication = authenticationManager.authenticate(UsernamePasswordAuthenticationToken(user.phoneNumber!!, user.passwordHash))
        if (!authentication.isAuthenticated) {
            logger.warn("Unsuccessful authorization for user ${user.phoneNumber}")
            throw SecurityException("Not authorized")
        }
        return userRepository.findUserByPhoneNumber(user.phoneNumber)!!
    }

    override fun registerUser(user: User): User {
        ValidationUtils.validateEntity(user)
        if(userRepository.findUserByPhoneNumber(user.phoneNumber!!) != null) {
            logger.warn("User already exists, phone = ${user.phoneNumber}")
            throw RequestValidationException("User with phone = ${user.phoneNumber} already exists")
        }
        val roles: Set<Role> = setOf(roleRepository.getById(3))
        val userToSave = User(null, passwordEncoder.encode(user.passwordHash), user.phoneNumber, user.email, user.userData, null, roles)
        transactionManager.begin()
        val userRepository = userRepository.save(userToSave)
        transactionManager.commit()
        return userRepository
    }
}