package oliveira.technological.solutions.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size


@Entity
data class Planet(
    @Id
    val id: String? = null,

    @field:NotEmpty
    @field:Size(min = 3, max = 150)
    val name: String,

    @field:NotEmpty
    @field:Size(max = 150)
    val climate: String,

    @field:NotEmpty
    @field:Size(max = 250)
    val terrain: String
)

