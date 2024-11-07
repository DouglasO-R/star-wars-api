package oliveira.technological.solutions.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity
data class Planet(
    @Id
    @GeneratedValue
    val id: Long,
    val name: String,
    val climate: String,
    val terrain: String
)

