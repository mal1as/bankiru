package com.itmo.blps.bankiru.repository

import com.itmo.blps.bankiru.model.Address
import org.springframework.data.jpa.repository.JpaRepository

interface AddressRepository : JpaRepository<Address, Long>