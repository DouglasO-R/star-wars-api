package oliveira.technological.solutions.controller

import oliveira.technological.solutions.model.Planet
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import kotlin.test.Test
import kotlin.test.assertEquals


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PlanetsControllerRestTemplate {

    @LocalServerPort
    private var port: Int = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    private val baseUrl = "http://localhost:"

    @Test
    fun `should Create A Planet`(): Unit {
        val planetToCreate = Planet(name = "Albania", climate = "tropical", terrain = "tropical forest")


        val requestEntity = HttpEntity(planetToCreate)


        val response = restTemplate.exchange(
            "$baseUrl$port/planets/2cec3d8e-0857-4769-b3ad-75069c73d42b",
            HttpMethod.PUT, requestEntity, String::class.java
        )

        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
    }
}