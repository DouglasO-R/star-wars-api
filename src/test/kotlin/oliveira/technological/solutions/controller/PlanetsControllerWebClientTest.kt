package oliveira.technological.solutions.controller

import oliveira.technological.solutions.model.Planet
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient
import kotlin.test.Test


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class PlanetsControllerWebClientTest {


    @Autowired
    lateinit var webTestClient: WebTestClient

    @Test
    fun `should Create A Planet`() {
        val planetToCreate = Planet(name = "Albania", climate = "tropical", terrain = "tropical forest")


        webTestClient.put().uri("/planets/2cec3d8e-0857-4769-b3ad-75069c73d42b")
            .bodyValue(planetToCreate)
            .exchange()
            .expectStatus().isNoContent

    }

}