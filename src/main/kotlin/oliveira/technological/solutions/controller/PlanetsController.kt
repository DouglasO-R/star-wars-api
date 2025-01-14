package oliveira.technological.solutions.controller

import jakarta.validation.ConstraintViolationException
import kotlinx.serialization.Serializable
import oliveira.technological.solutions.model.Planet
import oliveira.technological.solutions.repository.PlanetRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/planets")
class PlanetsController {
    @Autowired
    lateinit var planetRepository: PlanetRepository

    @GetMapping
    fun list(@RequestParam name: String = ""): Any {
        if (name != "") {
            val resultByName = planetRepository.findAllByNameContainingIgnoreCase(name)
            return ResponseEntity.ok(resultByName)
        }

        val result = planetRepository.findAll()
        return ResponseEntity.ok(result)
    }


    @GetMapping("{id}")
    fun searchById(
        @PathVariable id: String
    ): Any {
        val planet = planetRepository.findByIdOrNull(id) ?: return ResponseEntity.notFound().build<Void>()

        return ResponseEntity.ok(planet)
    }


    @PutMapping("{id}")
    fun createOrUpdate(
        @PathVariable id: String, @RequestBody body: Planet
    ): Any {
        val existingPlanet = planetRepository.findByName(body.name)
        if (existingPlanet.isNotEmpty() && existingPlanet.first().id != id) {
            return ResponseEntity.status(409).body(Error(409, "Planet already exists"))
        }

        val planet = body.copy(id = id)
        planetRepository.save(planet)

        return ResponseEntity.noContent().build<Void>()
    }

    @DeleteMapping("{id}")
    fun remove(
        @PathVariable id: String
    ): ResponseEntity<Any> {
            planetRepository.deleteById(id)
            return ResponseEntity.noContent().build()
    }
}


@Serializable
data class Error(val status: Int, val message: String)

@ControllerAdvice
class ErrorController{

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolatesException(ex:ConstraintViolationException):ResponseEntity<Error>{
        return ResponseEntity.status(400).body(Error(400, ex.constraintViolations.first().message!!))
    }
}

