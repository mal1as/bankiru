package com.itmo.blps.bankiru.service

import com.itmo.blps.bankiru.model.User
import org.springframework.stereotype.Service

@Service
interface AuthService {

    fun authUser(user: User): User

    fun registerUser(user: User): User
}