package oliveira.technological.solutions.controller

import io.ktor.http.*
import oliveira.technological.solutions.model.Planet
import oliveira.technological.solutions.repository.PlanetRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import kotlin.test.Test


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PlanetsControllerRestTemplate {

    @LocalServerPort
    private var port: Int = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    private val baseUrl = "http://localhost:"

    @Autowired
    lateinit var repository: PlanetRepository


    @BeforeEach()
    fun setupClean() {
        repository.deleteAll()
    }

    @Test
    fun `should Create A Planet`(): Unit {
        //given
        val planetToCreate = Planet(name = "Albania", climate = "tropical", terrain = "tropical forest")
        val id = "2cec3d8e-0857-4769-b3ad-75069c73d42b"
        val requestEntity = HttpEntity(planetToCreate)

        //when
        val response = restTemplate.exchange(
            "$baseUrl$port/planets/${id}",
            HttpMethod.PUT, requestEntity, String::class.java
        )

        //then
        assertThat(response.statusCode).isEqualTo(HttpStatus.NO_CONTENT)
    }

    @Test
    fun `should retrieve a planet`() {
        //given
        val planetToCreate = Planet(name = "Albania", climate = "tropical", terrain = "tropical forest")
        val id = "2cec3d8e-0857-4769-b3ad-75069c73d42b"
        val requestEntity = HttpEntity(planetToCreate)
        val planetCreated = planetToCreate.copy(id = id)

        restTemplate.exchange(
            "$baseUrl$port/planets/${id}",
            HttpMethod.PUT, requestEntity, String::class.java
        )
        //when
        val response = restTemplate.getForEntity(
            "$baseUrl$port/planets/${id}",
            Planet::class.java
        )
        //then

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(planetCreated)

    }

    @Test
    fun `should updated a planet`() {
//        given
        val planetToCreate = Planet(name = "Albania", climate = "tropical", terrain = "tropical forest")
        val id = "2cec3d8e-0857-4769-b3ad-75069c73d42b"
        val requestEntityToCreate = HttpEntity(planetToCreate)
        val planetToUpdate = planetToCreate.copy(terrain = "desert", id = id)
        val requestEntityToUpdate = HttpEntity(planetToUpdate)


        restTemplate.exchange(
            "$baseUrl$port/planets/${id}",
            HttpMethod.PUT, requestEntityToCreate, String::class.java
        )
//        when
        val response = restTemplate.exchange(
            "$baseUrl$port/planets/${id}",
            HttpMethod.PUT, requestEntityToUpdate, String::class.java
        )

        val updatedPlanet = restTemplate.restTemplate.getForEntity(
            "$baseUrl$port/planets/${id}",
            Planet::class.java
        )
//        then
        assertThat(response.statusCode).isEqualTo(HttpStatus.NO_CONTENT)
        assertThat(updatedPlanet.body).isEqualTo(planetToUpdate)
    }

    @Test
    fun `should delete a planet`() {
        //given
        val planetToCreate = Planet(name = "Albania", climate = "tropical", terrain = "tropical forest")
        val id = "2cec3d8e-0857-4769-b3ad-75069c73d42b"
        val requestEntityToCreate = HttpEntity(planetToCreate)
        restTemplate.exchange(
            "$baseUrl$port/planets/${id}",
            HttpMethod.PUT, requestEntityToCreate, String::class.java
        )

        //when
        val responseDelete = restTemplate.exchange(
            "$baseUrl$port/planets/${id}",
            HttpMethod.DELETE,
            null,
            Void::class.java
        )


        //then

        assertThat(responseDelete.statusCode).isEqualTo(HttpStatus.NO_CONTENT)
    }

    @Test
    fun `can not have 2 planets with the same name and id`() {
        //given
        val planetToCreate1 = Planet(name = "Kalinor", climate = "tropical", terrain = "tropical forest")
        val planetToCreate2 = Planet(name = "Luthar IV", climate = "Temperate, tropical", terrain = "Jungle, rainforests")
        val planetToCreateWithNameAlreadyExist = Planet(name = "Kalinor", climate = "arid", terrain = "desert")
        val planetToCreateWithoutClimate = Planet(name = "Ballatus", climate = "", terrain = "desert")
        val planetToCreateWithoutTerrain = Planet(name = "Kadmus", climate = "tropical", terrain = "")

        val requestEntityToCreatePlanet1 = HttpEntity(planetToCreate1)
        val requestEntityToCreatePlanet2 = HttpEntity(planetToCreate2)
        val requestEntityToCreateWithNameAlreadyExist = HttpEntity(planetToCreateWithNameAlreadyExist)
        val requestEntityToCreateWithoutClimate = HttpEntity(planetToCreateWithoutClimate)
        val requestEntityToCreateWithoutTerrain = HttpEntity(planetToCreateWithoutTerrain)

        val id1 = "13594cf8-94c6-4ae0-8f6b-dca86e44bbb4"
        val id2 = "2f1a5d4a-a3a2-4f62-9802-2a355c05e09b"
        val id3 = "b6a27d6b-4405-497a-80f4-58d326974ac2"
        val id4 = "3c898c83-f2c3-4ff6-86e3-92f01a978216"
        val id5 = "4a68be6a-39db-4509-8239-35935560db9d"

        val createdFirstPlanet = planetToCreate1.copy(id = id1)
        val createdSecondPlanet = planetToCreate2.copy(id = id2)
        val listPlanets = listOf(createdFirstPlanet, createdSecondPlanet)

        //when
        restTemplate.exchange(
            "$baseUrl$port/planets/${id1}",
            HttpMethod.PUT,
            requestEntityToCreatePlanet1,
            String::class.java
        )

        restTemplate.exchange(
            "$baseUrl$port/planets/${id2}",
            HttpMethod.PUT,
            requestEntityToCreatePlanet2,
            String::class.java
        )

        val responsePlanetToCreateWithNameAlreadyExist = restTemplate.exchange(
            "$baseUrl$port/planets/${id3}",
            HttpMethod.PUT,
            requestEntityToCreateWithNameAlreadyExist,
            Error::class.java
        )

        val responsePlanetToCreateWithoutClimate = restTemplate.exchange(
            "$baseUrl$port/planets/${id4}",
            HttpMethod.PUT,
            requestEntityToCreateWithoutClimate,
            Error::class.java
        )

        val responsePlanetToCreateWithoutTerrain = restTemplate.exchange(
            "$baseUrl$port/planets/${id5}",
            HttpMethod.PUT,
            requestEntityToCreateWithoutTerrain,
            Error::class.java
        )

        val retrieveResponse: ResponseEntity<List<Planet>> = restTemplate.exchange(
            "$baseUrl$port/planets",
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<List<Planet>>() {}
        )


        assertThat(retrieveResponse.body).isEqualTo(listPlanets)

        assertThat(responsePlanetToCreateWithNameAlreadyExist.statusCode).isEqualTo(HttpStatus.CONFLICT)
        assertThat(responsePlanetToCreateWithNameAlreadyExist.body).isEqualTo(Error(409, "Planet already exists"))

        assertThat(responsePlanetToCreateWithoutClimate.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(responsePlanetToCreateWithoutClimate.body).isEqualTo(Error(400, "não deve estar vazio"))


        assertThat(responsePlanetToCreateWithoutTerrain.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(responsePlanetToCreateWithoutTerrain.body).isEqualTo(Error(400, "não deve estar vazio"))

    }


}