package com.itmo.blps.bankiru.model

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.Min

@Entity
@Table(name = "credit_rule")
class CreditRule(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id")
    val id: Long?,

    @Column(name = "min_rate", nullable = false) @field:DecimalMin("0.0")
    val minRate: Double?,

    @Column(name = "max_rate", nullable = false) @field:DecimalMin("0.0")
    val maxRate: Double?,

    @Column(name = "min_period", nullable = false) @field:Min(1)
    val minPeriod: Long?,

    @Column(name = "max_period", nullable = false) @field:Min(1)
    val maxPeriod: Long?,

    @Column(name = "min_sum", nullable = false) @field:Min(1000)
    val minSum: Long?,

    @Column(name = "max_sum", nullable = false) @field:Min(1000)
    val maxSum: Long?,

    @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "credit_type_id", nullable = false)
    val creditType: CreditType?,

    @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "bank_id", nullable = true) @JsonIgnore
    var bank: Bank?,

    @OneToMany(mappedBy = "creditRule", cascade = [CascadeType.ALL], fetch = FetchType.LAZY) @JsonIgnore
    val creditRequests: Set<CreditRequest>?
)