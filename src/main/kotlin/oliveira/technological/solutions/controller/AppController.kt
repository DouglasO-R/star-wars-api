package oliveira.technological.solutions.controller

import oliveira.technological.solutions.model.Planet
import oliveira.technological.solutions.repository.PlanetRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/planets")
class AppController {
    @Autowired
    lateinit var planetRepository: PlanetRepository

    @GetMapping
    fun list(@RequestParam name: String = ""): Any {
        if (name != "") {
            val resultByName = planetRepository.findByName(name)
            return ResponseEntity.ok(resultByName)
        }

        val result = planetRepository.findAll()
        return ResponseEntity.ok(result)
    }

    //TODO: CREATE a route Get{ID} to obtain a resource by id


    @PutMapping("{id}")
    fun createOrUpdate(
        @PathVariable
        id: String,
        @RequestBody
        body: Planet
    ): Any {
        val planet = body.copy(id = id)
        planetRepository.save(planet)

        return ResponseEntity.noContent().build<Void>()
    }

    @DeleteMapping("{id}")
    fun remove(
        @PathVariable
        id: String
    ): ResponseEntity<Any> {
        if (planetRepository.existsById(id)) {
            planetRepository.deleteById(id)
            return ResponseEntity.noContent().build()
        }
        return ResponseEntity.notFound().build()
    }
}
