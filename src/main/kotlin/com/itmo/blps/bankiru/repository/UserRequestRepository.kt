package com.itmo.blps.bankiru.repository

import com.itmo.blps.bankiru.model.UserRequest
import org.springframework.data.jpa.repository.JpaRepository

interface UserRequestRepository : JpaRepository<UserRequest, Long>