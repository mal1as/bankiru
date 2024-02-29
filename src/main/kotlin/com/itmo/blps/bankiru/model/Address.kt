package com.itmo.blps.bankiru.model

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

@Entity
@Table(name = "address")
class Address(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id")
    val id: Long?,

    @Column(name = "country", nullable = false) @field:NotBlank
    val country: String?,

    @Column(name = "town", nullable = false) @field:NotBlank
    val town: String?,

    @Column(name = "street", nullable = false) @field:NotBlank
    val street: String?,

    @Column(name = "house_number", nullable = false) @field:Min(1)
    val houseNumber: Int?,

    @Column(name = "building_number", nullable = true) @field:Min(1)
    val buildingNumber: Int?,

    @Column(name = "office_number", nullable = true) @field:Min(1)
    val officeNumber: Int?,

    @ManyToMany(mappedBy = "addresses", fetch = FetchType.LAZY) @JsonIgnore
    var banks: Set<Bank>?
)