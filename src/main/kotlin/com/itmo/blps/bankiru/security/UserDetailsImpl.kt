package com.itmo.blps.bankiru.security

import com.itmo.blps.bankiru.model.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserDetailsImpl(
    private val id: Long,
    private val phoneNumber: String,
    private val password: String,
    private val authorities: MutableCollection<out GrantedAuthority>
) : UserDetails {

    companion object {
        fun build(user: User): UserDetailsImpl {
            val authorities = user.roles!!.map { SimpleGrantedAuthority(it.name) }.toMutableSet()
            return UserDetailsImpl(user.id!!, user.phoneNumber!!, user.passwordHash!!, authorities)
        }
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return authorities
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return phoneNumber
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}