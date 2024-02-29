package com.itmo.blps.bankiru.model

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

@Entity
@Table(name = "\"user\"")
class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id")
    val id: Long?,

    @Column(name = "password_hash", nullable = false) @field:NotBlank
    val passwordHash: String?,

    @Column(name = "phone_number", nullable = false)
    @field:Pattern(regexp = "^(\\s*)?(\\+)?([- _():=+]?\\d[- _():=+]?){10,14}(\\s*)?\$")
    val phoneNumber: String?,

    @Column(name = "email", nullable = false) @field:Email
    val email: String?,

    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER) @JoinColumn(name = "user_data_id", nullable = true)
    val userData: UserData?,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.LAZY) @JsonIgnore
    val userRequests: Set<UserRequest>?,

    @ManyToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    val roles: Set<Role>?
)