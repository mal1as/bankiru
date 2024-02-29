package com.itmo.blps.bankiru.model

import com.fasterxml.jackson.annotation.JsonIgnore
import io.swagger.annotations.ApiModelProperty
import javax.persistence.*
import javax.validation.constraints.DecimalMax
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

@Entity
@Table(name = "bank")
class Bank(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id")
    val id: Long?,

    @Column(name = "name", nullable = false) @field:NotBlank
    val name: String?,

    @Column(name = "description", nullable = true)
    val description: String?,

    @Column(name = "rate", nullable = true) @field:DecimalMin("0.0") @field:DecimalMax("5.0")
    val rate: Double?,

    @Column(name = "phone_number", nullable = false)
    @field:Pattern(regexp = "^(\\s*)?(\\+)?([- _():=+]?\\d[- _():=+]?){10,14}(\\s*)?\$")
    val phoneNumber: String?,

    @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "ownership_type_id", nullable = false)
    val ownershipType: OwnershipType?,

    @OneToMany(mappedBy = "bank", cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @ApiModelProperty(hidden = true)
    val creditRules: Set<CreditRule>?,

    @ManyToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinTable(
        name = "bank_addresses",
        joinColumns = [JoinColumn(name = "bank_id")],
        inverseJoinColumns = [JoinColumn(name = "address_id")]
    )
    var addresses: Set<Address>?,

    @OneToMany(mappedBy = "bank", cascade = [CascadeType.ALL], fetch = FetchType.LAZY) @JsonIgnore
    val creditRequests: Set<CreditRequest>?
)