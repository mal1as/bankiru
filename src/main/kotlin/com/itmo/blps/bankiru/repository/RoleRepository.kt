package com.itmo.blps.bankiru.repository

import com.itmo.blps.bankiru.model.Role
import org.springframework.data.jpa.repository.JpaRepository

interface RoleRepository : JpaRepository<Role, Long>