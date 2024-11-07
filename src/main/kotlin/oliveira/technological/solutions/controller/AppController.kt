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
    fun list(@RequestParam id: Long = 0, name: String = ""): Any {
        if (id.toInt() != 0) {
            val resultById = planetRepository.findById(id)
            return ResponseEntity.ok(resultById)
        } else if (name != "") {
            val resultByName = planetRepository.findByName(name)
            return ResponseEntity.ok(resultByName)
        }

        val result = planetRepository.findAll()
        return ResponseEntity.ok(result)
    }

    @PostMapping
    fun add(
        @RequestBody(required = false)
        planet: Planet
    ): ResponseEntity<out Any> {
        val planetAlreadyExist = planetRepository.findByName(planet.name)
        if (planetAlreadyExist == null) {
            val planetAdded =  planetRepository.save(planet)
            return ResponseEntity.ok(planetAdded)
        }
        return ResponseEntity.status(409).body("message:\"Planet already exists\"")

    }

    @PutMapping("{id}")
    fun update(
        @PathVariable
        id: Long,
        @RequestBody
        body: Planet
    ): Any {
        if (planetRepository.existsById(id)) {
            val planet = body.copy(id = id)
            val planetUpdated = planetRepository.save(planet)
            return ResponseEntity.noContent()
        }
        return ResponseEntity.status(409).body("message:\"Planet already exists\"")
    }

    @DeleteMapping("{id}")
    fun remove(
        @PathVariable
        id: Long
    ): ResponseEntity.HeadersBuilder<*> {
        if (planetRepository.existsById(id)) {
            planetRepository.deleteById(id)
            return ResponseEntity.noContent()

        }
        return ResponseEntity.notFound()
    }
}
