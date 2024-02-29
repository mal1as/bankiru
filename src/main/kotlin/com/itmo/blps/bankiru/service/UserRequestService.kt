package com.itmo.blps.bankiru.service

import com.itmo.blps.bankiru.model.UserRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
interface UserRequestService {

    fun getAllUserRequests(pageable: Pageable): Page<UserRequest>

    fun getUserRequestById(id: Long): UserRequest

    fun addUserRequest(userRequest: UserRequest): UserRequest

    fun removeUserRequestById(id: Long)
}