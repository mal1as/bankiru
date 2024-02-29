package com.itmo.blps.bankiru.model

import javax.persistence.*
import javax.validation.constraints.Min

@Entity
@Table(name = "user_request")
class UserRequest(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id")
    val id: Long?,

    @Column(name = "sum", nullable = false) @field:Min(1000)
    val sum: Long?,

    @Column(name = "period", nullable = false) @field:Min(1)
    val period: Long?,

    @Column(name = "deposit", nullable = false)
    val deposit: Boolean? = false,

    @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "user_id", nullable = false)
    val user: User?,

    @OneToMany(mappedBy = "userRequest", cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var creditRequests: Set<CreditRequest>?
)