package com.itmo.blps.bankiru.model

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity
@Table(name = "credit_request")
class CreditRequest(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id")
    val id: Long?,

    @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "request_status_id", nullable = false)
    val requestStatus: RequestStatus?,

    @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "bank_id", nullable = false)
    val bank: Bank?,

    @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "credit_rule_id", nullable = false)
    val creditRule: CreditRule?,

    @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "user_request_id", nullable = false) @JsonIgnore
    val userRequest: UserRequest?
)