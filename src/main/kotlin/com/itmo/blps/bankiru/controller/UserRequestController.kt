package com.itmo.blps.bankiru.controller

import com.itmo.blps.bankiru.dto.response.ErrorResponse
import com.itmo.blps.bankiru.dto.response.SuccessResponse
import com.itmo.blps.bankiru.exception.RequestValidationException
import com.itmo.blps.bankiru.model.User
import com.itmo.blps.bankiru.model.UserRequest
import com.itmo.blps.bankiru.security.jwt.JwtUtils
import com.itmo.blps.bankiru.service.UserRequestService
import com.itmo.blps.bankiru.service.UserService
import com.itmo.blps.bankiru.utils.RequestHeadersUtils.Companion.getAuthTokenWithoutPrefix
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import kotlin.reflect.full.declaredMemberProperties

@RestController
@RequestMapping("/api/v1/userRequest")
class UserRequestController(
    private val userRequestService: UserRequestService,
    private val userService: UserService,
    private val jwtUtils: JwtUtils
) {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Ok"),
            ApiResponse(code = 400, message = "Bad request", response = ErrorResponse::class),
            ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse::class)
        ]
    )
    @ApiOperation(value = "Get all user requests")
    @GetMapping
    fun getAllUserRequests(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "id") sortField: String,
        @RequestParam(defaultValue = "false") sortDesc: Boolean
    ): ResponseEntity<SuccessResponse<List<UserRequest>>> {
        if (!UserRequest::class.declaredMemberProperties.any { it.name == sortField }) {
            logger.warn("Sorting error, field $sortField not found in UserRequest class")
            throw RequestValidationException("Field $sortField not found in UserRequest class")
        }
        val userRequestPage = userRequestService.getAllUserRequests(
            PageRequest.of(page, size, Sort.by(if (sortDesc) Sort.Direction.DESC else Sort.Direction.ASC, sortField))
        )
        logger.info("Get all user requests operation success, page = $page, size = $size, sortField - $sortField, sort descending is $sortDesc")
        return ResponseEntity.ok(SuccessResponse(true, userRequestPage.toList(), userRequestPage.totalPages, userRequestPage.totalElements))
    }

    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Ok"),
            ApiResponse(code = 400, message = "Bad request", response = ErrorResponse::class),
            ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse::class)
        ]
    )
    @ApiOperation(value = "Get user request by id")
    @GetMapping("/{id}")
    fun getUserRequestById(@PathVariable id: Long, @RequestHeader("Authorization") token: String): ResponseEntity<SuccessResponse<UserRequest>> {
        val userRequest: UserRequest = userRequestService.getUserRequestById(id)
        if(!isAdminToken(getAuthTokenWithoutPrefix(token)) && jwtUtils.getClaimsFromToken(getAuthTokenWithoutPrefix(token)).subject != userRequest.user!!.phoneNumber) {
            logger.warn("Authorization error, user is not allowed to update this user request, user token = ${jwtUtils.getClaimsFromToken(getAuthTokenWithoutPrefix(token)).subject}, user request id = $id")
            throw RequestValidationException("Not allowed to update this user request")
        }
        logger.info("Get user request operation success, user id = ${jwtUtils.getClaimsFromToken(getAuthTokenWithoutPrefix(token)).subject}, request id = $id")
        return ResponseEntity.ok(SuccessResponse(content = userRequest))
    }

    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Ok"),
            ApiResponse(code = 400, message = "Bad request", response = ErrorResponse::class),
            ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse::class)
        ]
    )
    @ApiOperation(value = "Add user request", notes = "1. creditRequests must be null or empty")
    @PostMapping
    fun addUserRequest(@RequestBody userRequest: UserRequest, @RequestHeader("Authorization") token: String): ResponseEntity<SuccessResponse<UserRequest>> {
        val user: User = userService.findUserById(userRequest.user!!.id!!)
        if(!isAdminToken(getAuthTokenWithoutPrefix(token)) && jwtUtils.getClaimsFromToken(getAuthTokenWithoutPrefix(token)).subject != user.phoneNumber) {
            logger.warn("Authorization error, user is not allowed to add user request for this user, user token = $token, user request id = ${userRequest.id}")
            throw RequestValidationException("Not allowed to add request fot this user")
        }
        logger.info("Add user request operation success, user id = ${jwtUtils.getClaimsFromToken(getAuthTokenWithoutPrefix(token)).subject}")
        return ResponseEntity.ok(SuccessResponse(content = userRequestService.addUserRequest(userRequest)))
    }

    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Ok"),
            ApiResponse(code = 400, message = "Bad request", response = ErrorResponse::class),
            ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse::class)
        ]
    )
    @ApiOperation(value = "Delete user request by id")
    @DeleteMapping("/{id}")
    fun deleteUserRequestById(@PathVariable id: Long, @RequestHeader("Authorization") token: String): ResponseEntity<SuccessResponse<Any>> {
        val userRequest: UserRequest = userRequestService.getUserRequestById(id)
        if(!isAdminToken(getAuthTokenWithoutPrefix(token)) && jwtUtils.getClaimsFromToken(getAuthTokenWithoutPrefix(token)).subject != userRequest.user!!.phoneNumber) {
            logger.warn("Authorization error, user is not allowed to delete this user request, user token = $token, user request id = $id")
            throw RequestValidationException("Not allowed to delete this user request")
        }
        userRequestService.removeUserRequestById(id)
        logger.info("Delete user request operation success, user id = ${jwtUtils.getClaimsFromToken(getAuthTokenWithoutPrefix(token)).subject}, user request id = $id")
        return ResponseEntity.ok(SuccessResponse())
    }

    private fun isAdminToken(token: String): Boolean {
        return userService.findUserByPhone(jwtUtils.getClaimsFromToken(token).subject)!!.roles!!.any { it.name == "ADMIN" }
    }
}