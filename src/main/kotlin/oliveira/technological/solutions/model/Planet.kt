package oliveira.technological.solutions.model

import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
data class Planet(
    @Id
    val id: String? = null,
    val name: String,
    val climate: String,
    val terrain: String
)

