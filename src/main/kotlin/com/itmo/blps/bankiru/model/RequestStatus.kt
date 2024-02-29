package com.itmo.blps.bankiru.model

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
@Table(name = "request_status")
class RequestStatus(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id")
    val id: Long?,

    @Column(name = "name", nullable = false) @field:NotBlank
    val name: String?,

    @OneToMany(mappedBy = "requestStatus", cascade = [CascadeType.ALL], fetch = FetchType.LAZY) @JsonIgnore
    val creditRequests: Set<CreditRequest>?
)