package oliveira.technological.solutions.repository

import oliveira.technological.solutions.model.Planet
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface PlanetRepository: CrudRepository<Planet, Long> {
    fun findByName(name:String):Planet?
}