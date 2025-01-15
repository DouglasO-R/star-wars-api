package oliveira.technological.solutions.controller

import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.dotenv
import io.ktor.http.*
import oliveira.technological.solutions.model.Planet
import oliveira.technological.solutions.repository.PlanetRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.web.reactive.server.WebTestClient
import kotlin.test.Test


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class PlanetsControllerWebClientTest {


    @Autowired
    lateinit var webTestClient: WebTestClient

    @Autowired
    lateinit var repository: PlanetRepository

    companion object {
        @BeforeAll
        @JvmStatic
        fun loadEnv() {
            val dotenv = dotenv()

            System.setProperty("DATABASE_URL", dotenv["DATABASE_URL"])
            System.setProperty("DATABASE_USERNAME", dotenv["DATABASE_USERNAME"])
            System.setProperty("DATABASE_PASSWORD", dotenv["DATABASE_PASSWORD"])
        }
    }

    @BeforeEach()
    fun setupClean() {
        repository.deleteAll()
    }

    @Test
    fun `should Create A Planet`() {
        //Given
        val planetToCreate = Planet(name = "Albania", climate = "tropical", terrain = "tropical forest")

        //When
        val response = webTestClient.put()
            .uri("/planets/2cec3d8e-0857-4769-b3ad-75069c73d42b")
            .bodyValue(planetToCreate)
            .exchange()
            .expectBody()
            .returnResult()

        //Then
        assertThat(response.status).isEqualTo(HttpStatus.NO_CONTENT)
        assertThat(response.responseBody).isNull()

    }

    @Test
    fun `should Retrieve a planet`() {
        //Given
        val planetToCreate = Planet(name = "Albania", climate = "tropical", terrain = "tropical forest")
        val id = "2cec3d8e-0857-4769-b3ad-75069c73d42b"
        webTestClient.put()
            .uri("/planets/${id}")
            .bodyValue(planetToCreate)
            .exchange()
            .expectBody()
            .returnResult()

        //When
        val response = webTestClient.get()
            .uri("/planets/${id}")
            .exchange()
            .expectBody(Planet::class.java)
            .returnResult()

        val planetToRetrieve = planetToCreate.copy(id = id)

        //Then
        assertThat(response.status.value()).isEqualTo(HttpStatusCode.OK.value)
        assertThat(response.responseBody).isEqualTo(planetToRetrieve)
    }

    @Test
    fun `should update a planet`() {
        //given
        val planetToCreate = Planet(name = "Albania", climate = "tropical", terrain = "tropical forest")
        val id = "2cec3d8e-0857-4769-b3ad-75069c73d42b"
        val planetToUpdate = planetToCreate.copy(terrain = "desert", id = id)

        webTestClient.put()
            .uri("/planets/${id}")
            .bodyValue(planetToCreate)
            .exchange()
            .expectBody()
            .returnResult()

        //when
        val responseUpdate = webTestClient.put()
            .uri("/planets/${id}")
            .bodyValue(planetToUpdate)
            .exchange()
            .expectBody()
            .returnResult()

        val planetUpdated = webTestClient.get()
            .uri("/planets/${id}")
            .exchange()
            .expectBody(Planet::class.java)
            .returnResult()

        assertThat(responseUpdate.status.value()).isEqualTo(HttpStatusCode.NoContent.value)
        assertThat(responseUpdate.status).isEqualTo(HttpStatus.NO_CONTENT)

        assertThat(planetUpdated.responseBody).isEqualTo(planetToUpdate)
        assertThat(planetUpdated.responseBody?.id).isEqualTo(planetToUpdate.id)
        assertThat(planetUpdated.responseBody?.name).isEqualTo(planetToUpdate.name)
        assertThat(planetUpdated.responseBody?.terrain).isEqualTo(planetToUpdate.terrain)
        assertThat(planetUpdated.responseBody?.climate).isEqualTo(planetToUpdate.climate)

    }

    @Test
    fun `should delete a planet`() {
        //given
        val planetToCreate = Planet(name = "Albania", climate = "tropical", terrain = "tropical forest")
        val id = "2cec3d8e-0857-4769-b3ad-75069c73d42b"
        webTestClient.put()
            .uri("/planets/${id}")
            .bodyValue(planetToCreate)
            .exchange()
            .expectBody()
            .returnResult()

        //when
        val response = webTestClient.delete()
            .uri("/planets/${id}")
            .exchange()
            .expectBody()
            .returnResult()

        //then
        assertThat(response.status.value()).isEqualTo(HttpStatusCode.NoContent.value)
    }

    @Test
    fun `can not have 2 planets with the same name and id`() {
        //Given
        val planetToCreate1 = Planet(name = "Kalinor", climate = "tropical", terrain = "tropical forest")
        val planetToCreate2 = Planet(name = "Luthar IV", climate = "Temperate, tropical", terrain = "Jungle, rainforests")
        val planetToCreateWithNameAlreadyExist = Planet(name = "Kalinor", climate = "arid", terrain = "desert")
        val planetToCreateWithoutClimate = Planet(name = "Ballatus", climate = "", terrain = "desert")
        val planetToCreateWithoutTerrain = Planet(name = "Kadmus", climate = "tropical", terrain = "")

        val id1 = "13594cf8-94c6-4ae0-8f6b-dca86e44bbb4"
        val id2 = "2f1a5d4a-a3a2-4f62-9802-2a355c05e09b"
        val id3 = "b6a27d6b-4405-497a-80f4-58d326974ac2"
        val id4 = "3c898c83-f2c3-4ff6-86e3-92f01a978216"
        val id5 = "4a68be6a-39db-4509-8239-35935560db9d"

        val createdFirstPlanet = planetToCreate1.copy(id = id1)
        val createdSecondPlanet = planetToCreate1.copy(id = id2)
        val listPlanets = listOf(createdFirstPlanet,createdSecondPlanet)

        //when
        val responseFirstPlanet = webTestClient.put()
            .uri("/planets/${id1}")
            .bodyValue(planetToCreate1)
            .exchange()
            .expectBody()
            .returnResult()

        val responseSecondPlanet = webTestClient.put()
            .uri("/planets/${id2}")
            .bodyValue(planetToCreate2)
            .exchange()
            .expectBody()
            .returnResult()

        val responsePlanetToCreateWithNameAlreadyExist = webTestClient.put()
            .uri("/planets/${id3}")
            .bodyValue(planetToCreateWithNameAlreadyExist)
            .exchange()
            .expectBody(Error::class.java)
            .returnResult()

        val responsePlanetToCreateWithoutClimate = webTestClient.put()
            .uri("/planets/${id4}")
            .bodyValue(planetToCreateWithoutClimate)
            .exchange()
            .expectBody(Error::class.java)
            .returnResult()

        val responsePlanetToCreateWithoutTerrain = webTestClient.put()
            .uri("/planets/${id5}")
            .bodyValue(planetToCreateWithoutTerrain)
            .exchange()
            .expectBody(Error::class.java)
            .returnResult()

        // error
//        val retrieveResponse = webTestClient.get()
//            .uri("/planets")
//            .exchange()
//            .expectBodyList(Planet::class.java)
//            .returnResult()

        //then
//        assertThat(retrieveResponse.responseBody).isEqualTo(listPlanets)

        assertThat(responsePlanetToCreateWithNameAlreadyExist.status.value()).isEqualTo(HttpStatusCode.Conflict.value)
        assertThat(responsePlanetToCreateWithNameAlreadyExist.responseBody).isEqualTo(Error(409, "Planet already exists"))

        assertThat(responsePlanetToCreateWithoutClimate.status.value()).isEqualTo(HttpStatusCode.BadRequest.value)
        assertThat(responsePlanetToCreateWithoutClimate.responseBody).isEqualTo(Error(400, "não deve estar vazio"))


        assertThat(responsePlanetToCreateWithoutTerrain.status.value()).isEqualTo(HttpStatusCode.BadRequest.value)
        assertThat(responsePlanetToCreateWithoutTerrain.responseBody).isEqualTo(Error(400, "não deve estar vazio"))


    }
}