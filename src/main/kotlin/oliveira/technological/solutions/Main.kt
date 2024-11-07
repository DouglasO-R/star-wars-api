package oliveira.technological.solutions

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import oliveira.technological.solutions.model.Planet
import oliveira.technological.solutions.repository.PlanetRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.web.bind.annotation.*

@SpringBootApplication
class App {

}

fun main(args: Array<String>) {
    runApplication<App>(*args)
}






