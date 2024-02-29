package com.itmo.blps.bankiru.utils

import javax.validation.ConstraintViolation
import javax.validation.ConstraintViolationException
import javax.validation.Validation
import javax.validation.Validator

class ValidationUtils {
    companion object {

        fun <T> validateEntity(t: T) {
            val validator: Validator = Validation.buildDefaultValidatorFactory().validator
            val constraintViolations: Set<ConstraintViolation<T>> = validator.validate(t)
            if (constraintViolations.isNotEmpty()) {
                throw ConstraintViolationException(constraintViolations)
            }
        }
    }
}