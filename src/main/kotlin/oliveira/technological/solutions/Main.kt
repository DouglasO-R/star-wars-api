package oliveira.technological.solutions

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
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


@Entity
data class Planet(
    @Id
    @GeneratedValue
    val id: Long,
    val name: String,
    val climate: String,
    val terrain: String
)

@Repository
interface PlanetRepository: CrudRepository<Planet, Long>{
    fun findByName(name:String):List<Planet>
}


@RestController
@RequestMapping("/planets")
class AppController() {
    @Autowired
    lateinit var planetRepository:PlanetRepository

    @GetMapping()
    fun list(): MutableIterable<Planet> {
        return planetRepository.findAll()
    }

    @PostMapping()
    fun add(
        @RequestBody(required = false)
        planet: Planet
    ): Planet {
        return planetRepository.save(planet)
    }

    @PutMapping("{id}")
    fun update(
        @PathVariable
        id: Long,
        @RequestBody
        body: Planet
    ): Planet? {
         if (planetRepository.existsById(id)){
             val planet = body.copy(id = id)
             return planetRepository.save(planet)
         }
        return null
    }

    @DeleteMapping("{id}")
    fun remove(
        @PathVariable
        id: Long
    ){
        if (planetRepository.existsById(id)){
            planetRepository.deleteById(id)
        }

    }

//    @GetMapping("{id}")
//    fun searchById(@PathVariable("id") id: Long): Optional<Planet> {
//        return planetRepository.findById(id)
//    }
//
//    @GetMapping("{name}")
//    fun searchByName(@PathVariable("name") name:String): List<Planet> {
//        return planetRepository.findByName(name)
//    }
}

