package com.itmo.blps.bankiru.service.impl

import com.itmo.blps.bankiru.exception.RequestValidationException
import com.itmo.blps.bankiru.jta.CustomTransactionManager
//import com.itmo.blps.bankiru.jta.CustomTransactionManager
import com.itmo.blps.bankiru.model.Role
import com.itmo.blps.bankiru.model.User
import com.itmo.blps.bankiru.repository.RoleRepository
import com.itmo.blps.bankiru.repository.UserRepository
import com.itmo.blps.bankiru.service.UserService
import com.itmo.blps.bankiru.utils.ValidationUtils.Companion.validateEntity
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val transactionManager: CustomTransactionManager,
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository
) : UserService {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    override fun getAllUsers(pageable: Pageable): Page<User> {
        return userRepository.findAll(pageable)
    }

    override fun findUserById(id: Long): User {
        return userRepository.findById(id).get()
    }

    override fun findUserByPhone(phone: String): User? {
        return userRepository.findUserByPhoneNumber(phone)
    }

    override fun addUser(user: User): User {
        if (user.id != null) {
            logger.warn("Add user operation error, received not null user id, user id must be null")
            throw RequestValidationException("User id must be null")
        }
        if(findUserByPhone(user.phoneNumber!!) != null) {
            logger.warn("Add user operation error, user with phone number already exists, phone number = ${user.phoneNumber}")
            throw RequestValidationException("User with phone = ${user.phoneNumber} already exists")
        }
        transactionManager.begin()
        val persistUser = persistUser(user)
        transactionManager.commit()
        return persistUser

    }

    override fun updateUser(user: User): User {
        if (user.id == null || !userRepository.findById(user.id).isPresent) {
            logger.warn("User update operation error, user with given id not exists, id = ${user.id}")
            throw RequestValidationException("User with id = ${user.id} not exists")
        }
        transactionManager.begin()
        val persistUser = persistUser(user)
        transactionManager.commit()
        return persistUser
    }

    override fun removeUserById(id: Long) {
        transactionManager.begin()
        userRepository.deleteById(id)
        transactionManager.commit()
    }

    private fun persistUser(user: User): User {
        validateEntity(user)
        val roles: Set<Role> = if(user.roles == null) setOf() else user.roles.map { roleRepository.findById(it.id!!).get() }.toSet()
        return userRepository.save(User(user.id, user.passwordHash, user.phoneNumber, user.email, user.userData, null, roles))
    }
}