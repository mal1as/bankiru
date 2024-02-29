package com.itmo.blps.bankiru.repository

import com.itmo.blps.bankiru.model.RequestStatus
import org.springframework.data.jpa.repository.JpaRepository

interface RequestStatusRepository : JpaRepository<RequestStatus, Long>