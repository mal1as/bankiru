package com.itmo.blps.bankiru.service

import com.itmo.blps.bankiru.model.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
interface UserService {

    fun getAllUsers(pageable: Pageable): Page<User>

    fun findUserById(id: Long): User

    fun findUserByPhone(phone: String): User?

    fun addUser(user: User): User

    fun updateUser(user: User): User

    fun removeUserById(id: Long)
}