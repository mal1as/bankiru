package com.itmo.blps.bankiru.controller

import com.itmo.blps.bankiru.dto.response.ErrorResponse
import com.itmo.blps.bankiru.dto.response.SuccessResponse
import com.itmo.blps.bankiru.exception.RequestValidationException
import com.itmo.blps.bankiru.model.User
import com.itmo.blps.bankiru.service.UserService
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
@RequestMapping("/api/v1/user")
class UserController(
    private val userService: UserService
) {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Ok", response = User::class, responseContainer = "List"),
            ApiResponse(code = 400, message = "Bad request", response = ErrorResponse::class),
            ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse::class)
        ]
    )
    @ApiOperation(value = "Get all users")
    @GetMapping
    fun getAllUsers(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "id") sortField: String,
        @RequestParam(defaultValue = "false") sortDesc: Boolean
    ): ResponseEntity<SuccessResponse<List<User>>> {
        if (!User::class.declaredMemberProperties.any { it.name == sortField }) {
            logger.warn("Sorting error, field $sortField not found in User class")
            throw RequestValidationException("Field $sortField not found in User class")
        }
        val userPage = userService.getAllUsers(
            PageRequest.of(page, size, Sort.by(if (sortDesc) Sort.Direction.DESC else Sort.Direction.ASC, sortField))
        )
        logger.info("Get all users operation success, page = $page, size = $size, sortField = $sortField, sort descending is $sortDesc")
        return ResponseEntity.ok(SuccessResponse(true, userPage.toList(), userPage.totalPages, userPage.totalElements))
    }

    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Ok"),
            ApiResponse(code = 400, message = "Bad request", response = ErrorResponse::class),
            ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse::class)
        ]
    )
    @ApiOperation(value = "Get user by id")
    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Long): ResponseEntity<SuccessResponse<User>> {
        logger.info("Get user operation success, id = $id")
        return ResponseEntity.ok(SuccessResponse(content = userService.findUserById(id)))
    }

    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Ok"),
            ApiResponse(code = 400, message = "Bad request", response = ErrorResponse::class),
            ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse::class)
        ]
    )
    @ApiOperation(value = "Add user")
    @PostMapping
    fun addUser(@RequestBody user: User): ResponseEntity<SuccessResponse<User>> {
        logger.info("Add user operation success")
        return ResponseEntity.ok(SuccessResponse(content = userService.addUser(user)))
    }

    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Ok"),
            ApiResponse(code = 400, message = "Bad request", response = ErrorResponse::class),
            ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse::class)
        ]
    )
    @ApiOperation(value = "Update user")
    @PutMapping
    fun updateUser(@RequestBody user: User): ResponseEntity<SuccessResponse<User>> {
        logger.info("Update user operation success, id = ${user.id}")
        return ResponseEntity.ok(SuccessResponse(content = userService.updateUser(user)))
    }

    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Ok"),
            ApiResponse(code = 400, message = "Bad request", response = ErrorResponse::class),
            ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse::class)
        ]
    )
    @ApiOperation(value = "Delete user by id")
    @DeleteMapping("/{id}")
    fun deleteUserById(@PathVariable id: Long): ResponseEntity<SuccessResponse<Any>> {
        userService.removeUserById(id)
        logger.info("Delete user operation success, id = $id")
        return ResponseEntity.ok(SuccessResponse())
    }
}