package com.itmo.blps.bankiru.model

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.security.core.GrantedAuthority
import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
@Table(name = "role")
class Role (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id")
    val id: Long?,

    @Column(name = "name") @field:NotBlank
    val name: String?,

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY) @JsonIgnore
    val users: Set<User>?
) : GrantedAuthority {

    override fun getAuthority(): String {
        return name!!
    }
}