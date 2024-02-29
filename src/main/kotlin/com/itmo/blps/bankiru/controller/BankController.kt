package com.itmo.blps.bankiru.controller

import com.itmo.blps.bankiru.dto.response.ErrorResponse
import com.itmo.blps.bankiru.dto.response.SuccessResponse
import com.itmo.blps.bankiru.exception.RequestValidationException
import com.itmo.blps.bankiru.model.Address
import com.itmo.blps.bankiru.model.Bank
import com.itmo.blps.bankiru.model.CreditRule
import com.itmo.blps.bankiru.service.BankService
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import kotlin.reflect.full.declaredMemberProperties

@RestController
@RequestMapping("/api/v1/bank")
class BankController(
    private val bankService: BankService
) {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Ok"),
            ApiResponse(code = 400, message = "Bad request", response = ErrorResponse::class),
            ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse::class)
        ]
    )
    @ApiOperation(value = "Get all banks")
    @GetMapping
    fun getAllBanks(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "id") sortField: String,
        @RequestParam(defaultValue = "false") sortDesc: Boolean
    ): ResponseEntity<SuccessResponse<List<Bank>>> {
        if (!Bank::class.declaredMemberProperties.any { it.name == sortField }) {
            logger.warn("Sorting error, field $sortField not found in Bank class")
            throw RequestValidationException("Field $sortField not found in Bank class")
        }
        val bankPage: Page<Bank> = bankService.getAllBanks(
            PageRequest.of(page, size, Sort.by(if (sortDesc) Direction.DESC else Direction.ASC, sortField))
        )
        logger.info("Get all banks operation success, page = ${page}, size = ${size}, sortField = $sortField, sort descending is $sortDesc")
        return ResponseEntity.ok(SuccessResponse(true, bankPage.toList(), bankPage.totalPages, bankPage.totalElements))
    }

    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Ok"),
            ApiResponse(code = 400, message = "Bad request", response = ErrorResponse::class),
            ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse::class)
        ]
    )
    @ApiOperation(value = "Get bank by id")
    @GetMapping("/{id}")
    fun getBankById(@PathVariable id: Long): ResponseEntity<SuccessResponse<Bank>> {
        logger.info("Get bank operation success, id = $id")
        return ResponseEntity.ok(SuccessResponse(content = bankService.getBankById(id)))
    }

    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Ok"),
            ApiResponse(code = 400, message = "Bad request", response = ErrorResponse::class),
            ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse::class)
        ]
    )
    @ApiOperation(
        value = "Add bank", notes = "1. Bank id must be null\n" +
                "2. Add bank with addresses, do not add credit rules here\n" +
                "3. Use new address with id = null or existed address with id != null\n" +
                "4. Existed addresses won't be updated"
    )
    @PostMapping
    fun addBank(@RequestBody bank: Bank): ResponseEntity<SuccessResponse<Bank>> {
        logger.info("Add bank operation success")
        return ResponseEntity.ok(SuccessResponse(content = bankService.addBank(bank)))
    }

    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Ok"),
            ApiResponse(code = 400, message = "Bad request", response = ErrorResponse::class),
            ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse::class)
        ]
    )
    @ApiOperation(
        value = "Update bank", notes = "1. Bank id must be not null\n" +
                "2. Update bank with addresses, do not add credit rules here\n" +
                "3. Use new address with id = null or existed address with id != null\n" +
                "4. Existed addresses won't be updated"
    )
    @PutMapping
    fun updateBank(@RequestBody bank: Bank): ResponseEntity<SuccessResponse<Bank>> {
        logger.info("Update bank operation success, id = ${bank.id} ")
        return ResponseEntity.ok(SuccessResponse(content = bankService.updateBank(bank)))
    }

    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Ok"),
            ApiResponse(code = 400, message = "Bad request", response = ErrorResponse::class),
            ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse::class)
        ]
    )
    @ApiOperation(value = "Delete bank by id")
    @DeleteMapping("/{id}")
    fun deleteBankById(@PathVariable id: Long): ResponseEntity<SuccessResponse<Any>> {
        bankService.removeBankById(id)
        logger.info("Remove bank operation success, id = $id")
        return ResponseEntity.ok(SuccessResponse())
    }

    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Ok"),
            ApiResponse(code = 400, message = "Bad request", response = ErrorResponse::class),
            ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse::class)
        ]
    )
    @ApiOperation(
        value = "Add credit rules to bank",
        notes = "1. Only new credit rules are available to add"
    )
    @PostMapping("/{id}/creditRule")
    fun addCreditRulesToBank(@PathVariable id: Long, @RequestBody creditRules: Set<CreditRule>): ResponseEntity<SuccessResponse<Any>> {
        bankService.addCreditRulesToBank(id, creditRules)
        logger.info("Add credit rule operation success, bank id = $id, credit rule ids = ${creditRules.joinToString { it.id.toString() }}")
        return ResponseEntity.ok(SuccessResponse())
    }

    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Ok"),
            ApiResponse(code = 400, message = "Bad request", response = ErrorResponse::class),
            ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse::class)
        ]
    )
    @ApiOperation(
        value = "Add addresses to bank",
        notes = "1. Use new address with id = null or existed address with id != null\n" +
                "2. Existed addresses won't be updated"
    )
    @PostMapping("/{id}/address")
    fun addAddressesToBank(@PathVariable id: Long, @RequestBody addresses: Set<Address>): ResponseEntity<SuccessResponse<Any>> {
        bankService.addAddressesToBank(id, addresses)
        logger.info("Add addresses to bank operation success, bank id = $id, address ids = ${addresses.joinToString { it.id.toString() }}")
        return ResponseEntity.ok(SuccessResponse())
    }

    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Ok"),
            ApiResponse(code = 400, message = "Bad request", response = ErrorResponse::class),
            ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse::class)
        ]
    )
    @ApiOperation(value = "Delete address from bank")
    @DeleteMapping("/{bankId}/{addressId}")
    fun deleteAddressFromBank(@PathVariable bankId: Long, @PathVariable addressId: Long): ResponseEntity<SuccessResponse<Any>> {
        bankService.removeAddressFromBank(bankId, addressId)
        logger.info("Remove address from bank operation success, bank id = $bankId, address id = $addressId")
        return ResponseEntity.ok(SuccessResponse())
    }
}