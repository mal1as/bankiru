package com.itmo.blps.bankiru.controller

import com.itmo.blps.bankiru.dto.response.ErrorResponse
import com.itmo.blps.bankiru.dto.response.SuccessResponse
import com.itmo.blps.bankiru.service.CreditRequestService
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/creditRequest")
class CreditRequestController(
    private val creditRequestService: CreditRequestService
) {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Ok"),
            ApiResponse(code = 400, message = "Bad request", response = ErrorResponse::class),
            ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse::class)
        ]
    )
    @ApiOperation(value = "Update status")
    @PutMapping("/status")
    fun updateStatus(@RequestParam id: Long, @RequestParam statusId: Long): ResponseEntity<SuccessResponse<Any>> {
        creditRequestService.updateCreditRequestStatus(id, statusId)
        logger.info("Credit request update status operation success, credit request id = $id, status id = $statusId")
        return ResponseEntity.ok(SuccessResponse())
    }
}