package com.itmo.blps.bankiru.repository

import com.itmo.blps.bankiru.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {

    fun findUserByEmail(email: String): User?

    fun findUserByPhoneNumber(phoneNumber: String): User?
}