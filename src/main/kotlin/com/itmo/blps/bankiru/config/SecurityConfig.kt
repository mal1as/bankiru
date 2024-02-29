package com.itmo.blps.bankiru.config

import com.itmo.blps.bankiru.security.jwt.JwtFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.jaas.AbstractJaasAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.client.RestTemplate
import javax.servlet.http.HttpServletResponse

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig(
    private val jaasAuthenticationProvider: AbstractJaasAuthenticationProvider,
    private val jwtFilter: JwtFilter
) : WebSecurityConfigurerAdapter() {

    private val adminRole: String = "ADMIN"
    private val bankWorkerRole: String = "BANK_WORKER"
    private val userRole: String = "AUTH_USER"

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun restTemplate(): RestTemplate {
        return RestTemplate()
    }

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth!!.authenticationProvider(jaasAuthenticationProvider)
    }

    @Bean
    override fun authenticationManager(): AuthenticationManager {
        return super.authenticationManager()
    }

    override fun configure(web: WebSecurity?) {
        web!!.ignoring().antMatchers("/v2/api-docs",
            "/configuration/ui",
            "/swagger-resources/**",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**")
    }

    override fun configure(http: HttpSecurity?) {
        http!!
            .cors().disable()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .exceptionHandling().authenticationEntryPoint { _, response, authException ->
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.message) }.and()
            .authorizeRequests()
            .antMatchers(HttpMethod.POST,"/api/v1/auth/login", "/api/v1/auth/register").permitAll()
            .antMatchers(HttpMethod.GET, "/api/v1/bank/**").permitAll()
            .antMatchers(HttpMethod.POST,"/api/v1/bank/**").hasAnyAuthority(adminRole)
            .antMatchers(HttpMethod.PUT,"/api/v1/bank/**").hasAnyAuthority(adminRole)
            .antMatchers(HttpMethod.DELETE,"/api/v1/bank/**").hasAnyAuthority(adminRole)
            .antMatchers("/api/v1/creditRequest/**").hasAnyAuthority(bankWorkerRole)
            .antMatchers("/api/v1/user/**").hasAnyAuthority(adminRole)
            .antMatchers(HttpMethod.GET,"/api/v1/userRequest").hasAnyAuthority(adminRole)
            .antMatchers("/api/v1/userRequest/**").hasAnyAuthority(adminRole, userRole)
            .anyRequest().authenticated().and()
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter::class.java)
    }
}