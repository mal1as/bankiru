package com.itmo.blps.bankiru.security.jwt

import com.itmo.blps.bankiru.security.UserDetailsServiceImpl
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtUtils(
    @Value("\${secret.key}") private val jwtSecret: String,
    @Value("\${token.expire_time}") private val jwtExpirationMs: Long,
    private val userDetailsService: UserDetailsServiceImpl
) {

    fun generateToken(phoneNumber: String): String {
        val now = Date()
        val expiredDate = Date(now.time + jwtExpirationMs)
        val userDetails: UserDetails = userDetailsService.loadUserByUsername(phoneNumber)
        return Jwts.builder()
            .setSubject(phoneNumber)
            .setIssuedAt(now)
            .setExpiration(expiredDate)
            .addClaims(mapOf(Pair("roles", userDetails.authorities.map { it.authority })))
            .signWith(Keys.hmacShaKeyFor(jwtSecret.toByteArray()))
            .compact()
    }

    fun checkToken(token: String): Boolean {
        return try {
            val parser = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.toByteArray()))
                .build()
            parser.parseClaimsJws(token)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun getClaimsFromToken(token: String): Claims {
        val parser = Jwts.parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.toByteArray()))
            .build()
        return parser.parseClaimsJws(token).body
    }
}