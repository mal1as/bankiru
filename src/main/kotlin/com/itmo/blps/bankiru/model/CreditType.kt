package com.itmo.blps.bankiru.model

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
@Table(name = "credit_type")
class CreditType(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id")
    val id: Long?,

    @Column(name = "name", nullable = false) @field:NotBlank
    val name: String?,

    @Column(name = "deposit", nullable = false)
    val deposit: Boolean?,

    @OneToMany(mappedBy = "creditType", cascade = [CascadeType.ALL], fetch = FetchType.LAZY) @JsonIgnore
    val creditRules: Set<CreditRule>?
)