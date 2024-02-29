package com.itmo.blps.bankiru.exception

import javax.validation.ValidationException

class RequestValidationException(message: String) : ValidationException(message)