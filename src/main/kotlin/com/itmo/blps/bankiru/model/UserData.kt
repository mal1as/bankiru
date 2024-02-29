package com.itmo.blps.bankiru.model

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank

@Entity
@Table(name = "user_data")
class UserData(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id")
    val id: Long?,

    @Column(name = "name", nullable = false) @field:NotBlank
    val name: String?,

    @Column(name = "surname", nullable = false) @field:NotBlank
    val surname: String?,

    @Column(name = "patronymic", nullable = true)
    val patronymic: String?,

    @Column(name = "profit_per_month", nullable = true) @field:Min(0)
    val profitPerMonth: Long?,

    @Column(name = "work_company", nullable = true)
    val workCompany: String?,

    @OneToOne(mappedBy = "userData", cascade = [CascadeType.ALL], fetch = FetchType.LAZY) @JsonIgnore
    val user: User?
)