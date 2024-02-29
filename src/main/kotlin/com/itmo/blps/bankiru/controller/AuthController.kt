package com.itmo.blps.bankiru.controller

import com.itmo.blps.bankiru.dto.request.RefreshTokenRequest
import com.itmo.blps.bankiru.dto.response.ErrorResponse
import com.itmo.blps.bankiru.dto.response.UserTokenResponse
import com.itmo.blps.bankiru.dto.response.SuccessResponse
import com.itmo.blps.bankiru.exception.RequestValidationException
import com.itmo.blps.bankiru.model.User
import com.itmo.blps.bankiru.security.jwt.JwtUtils
import com.itmo.blps.bankiru.service.AuthService
import com.itmo.blps.bankiru.service.UserService
import com.itmo.blps.bankiru.utils.RequestHeadersUtils.Companion.getAuthTokenWithoutPrefix
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authService: AuthService,
    private val userService: UserService,
    private val jwtUtils: JwtUtils
) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Ok", response = UserTokenResponse::class),
            ApiResponse(code = 400, message = "Bad request", response = ErrorResponse::class),
            ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse::class)
        ]
    )
    @ApiOperation(value = "Login", response = UserTokenResponse::class)
    @PostMapping("/login")
    fun authUser(@RequestBody user: User): ResponseEntity<UserTokenResponse> {
        val authUser: User = authService.authUser(user)
        logger.info("Login success, phone = ${authUser.phoneNumber}")
        return ResponseEntity.ok(UserTokenResponse(jwtUtils.generateToken(authUser.phoneNumber!!)))
    }

    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Ok", response = UserTokenResponse::class),
            ApiResponse(code = 400, message = "Bad request", response = ErrorResponse::class),
            ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse::class)
        ]
    )
    @ApiOperation(value = "Register new user", response = UserTokenResponse::class)
    @PostMapping("/register")
    fun registerUser(@RequestBody user: User): ResponseEntity<UserTokenResponse> {
        val newUser: User = authService.registerUser(user)
        logger.info("Registration success, phone = ${newUser.phoneNumber}")
        return ResponseEntity.ok(UserTokenResponse(jwtUtils.generateToken(newUser.phoneNumber!!)))
    }

    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Ok", response = UserTokenResponse::class),
            ApiResponse(code = 400, message = "Bad request", response = ErrorResponse::class),
            ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse::class)
        ]
    )
    @ApiOperation(value = "Refresh auth token", response = UserTokenResponse::class)
    @PostMapping("/refreshToken")
    fun refreshToken(@RequestBody refreshTokenRequest: RefreshTokenRequest): ResponseEntity<UserTokenResponse> {
        val phoneNumber: String = jwtUtils.getClaimsFromToken(refreshTokenRequest.token).subject
        logger.info("Generating new auth token for user with phone = $phoneNumber")
        return ResponseEntity.ok(UserTokenResponse(jwtUtils.generateToken(phoneNumber)))
    }

    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Ok"),
            ApiResponse(code = 400, message = "Bad request", response = ErrorResponse::class),
            ApiResponse(code = 500, message = "Internal server error", response = ErrorResponse::class)
        ]
    )
    @ApiOperation(value = "Update user info")
    @PutMapping
    fun updateUserInfo(@RequestBody user: User, @RequestHeader("Authorization") token: String): ResponseEntity<SuccessResponse<User>> {
        if(jwtUtils.getClaimsFromToken(getAuthTokenWithoutPrefix(token)).subject != user.phoneNumber) {
            throw RequestValidationException("Not allowed to update this user")
        }
        return ResponseEntity.ok(SuccessResponse(content = userService.updateUser(user)))
    }
}