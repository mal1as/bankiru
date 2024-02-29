package com.itmo.blps.bankiru.model

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
@Table(name = "ownership_type")
class OwnershipType(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id")
    val id: Long?,

    @Column(name = "name", nullable = false) @field:NotBlank
    val name: String?,

    @Column(name = "short_name", nullable = false) @field:NotBlank
    val shortName: String?,

    @OneToMany(mappedBy = "ownershipType", cascade = [CascadeType.ALL], fetch = FetchType.LAZY) @JsonIgnore
    val banks: Set<Bank>?
)