package com.itmo.blps.bankiru.exception

import org.springframework.security.core.AuthenticationException

class SecurityException(message: String) : AuthenticationException(message)