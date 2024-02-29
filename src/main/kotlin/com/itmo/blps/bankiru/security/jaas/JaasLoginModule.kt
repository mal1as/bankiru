package com.itmo.blps.bankiru.security.jaas

import com.itmo.blps.bankiru.exception.RequestValidationException
import com.itmo.blps.bankiru.model.User
import com.itmo.blps.bankiru.repository.UserRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import java.nio.file.attribute.UserPrincipal
import java.security.Principal
import javax.security.auth.Subject
import javax.security.auth.callback.CallbackHandler
import javax.security.auth.callback.NameCallback
import javax.security.auth.callback.PasswordCallback
import javax.security.auth.spi.LoginModule

class JaasLoginModule : LoginModule {

    private val passwordEncoder: PasswordEncoder = BCryptPasswordEncoder()
    private var phoneNumber: String? = null
    private var loginSucceeded: Boolean = false
    private var subject: Subject? = null
    private var callbackHandler: CallbackHandler? = null
    private var userRepository: UserRepository? = null

    override fun initialize(subject: Subject?, callbackHandler: CallbackHandler?, sharedState: MutableMap<String, *>?, options: MutableMap<String, *>?) {
        this.callbackHandler = callbackHandler
        this.subject = subject
        this.userRepository = options!!["userRepository"] as UserRepository
    }

    override fun login(): Boolean {
        val nameCallback = NameCallback("username")
        val passwordCallback = PasswordCallback("password", false)

        callbackHandler!!.handle(arrayOf(nameCallback, passwordCallback))
        phoneNumber = nameCallback.name
        val password: String = passwordCallback.password.joinToString("")
        val user: User = userRepository!!.findUserByPhoneNumber(phoneNumber!!)
            ?: throw RequestValidationException("User not found by phone: $phoneNumber")
        loginSucceeded = passwordEncoder.matches(password, user.passwordHash)
        return loginSucceeded
    }

    override fun commit(): Boolean {
        if(!loginSucceeded) return false
        if(phoneNumber == null) throw RequestValidationException("Phone is null")
        val principal: Principal = UserPrincipal { phoneNumber }
        subject!!.principals.add(principal)
        return true
    }

    override fun abort(): Boolean {
        return false
    }

    override fun logout(): Boolean {
        return false
    }
}