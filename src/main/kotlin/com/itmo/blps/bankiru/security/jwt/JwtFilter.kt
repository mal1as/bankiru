package com.itmo.blps.bankiru.security.jwt

import io.jsonwebtoken.Claims
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtFilter(
    private val jwtUtils: JwtUtils
) : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val token: String? = getTokenFromRequest(request)
        if(token != null && jwtUtils.checkToken(token)) {
            val claims: Claims = jwtUtils.getClaimsFromToken(token)
            val phoneNumber: String = claims.subject
            val authorities: MutableCollection<out GrantedAuthority> = claims.get("roles", List::class.java)
                .map { SimpleGrantedAuthority(it.toString()) }.toMutableSet()

            val authentication = UsernamePasswordAuthenticationToken(phoneNumber, token, authorities)
            SecurityContextHolder.getContext().authentication = authentication
        }
        filterChain.doFilter(request, response)
    }

    private fun getTokenFromRequest(request: HttpServletRequest): String? {
        val bearer: String = request.getHeader("Authorization") ?: return null
        return if(bearer.startsWith("Bearer ")) bearer.substring(7) else null
    }
}