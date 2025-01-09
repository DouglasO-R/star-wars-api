package oliveira.technological.solutions.controller

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import jakarta.validation.ConstraintViolationException
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import oliveira.technological.solutions.model.Planet
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import kotlin.test.Test


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PlanetsControllerTest {

    @LocalServerPort
    lateinit var port: String

    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
    }

    @Test
    fun shouldCreateRetrieveAndRemoveAPlanet(): Unit = runBlocking {
        val planetToCreate = Planet(name = "Albania", climate = "tropical", terrain = "tropical forest")
        val listCreatedPlanet: List<Planet> = listOf(
            Planet(
                id = "2cec3d8e-0857-4769-b3ad-75069c73d42b",
                name = "Albania",
                climate = "tropical",
                terrain = "tropical forest"
            )
        )

        val planetToUpdate = Planet(name = "Albania", climate = "Desert", terrain = "Desert")
        val listUpdatedPlanet: List<Planet> = listOf(
            Planet(
                id = "2cec3d8e-0857-4769-b3ad-75069c73d42b",
                name = "Albania",
                climate = "Desert",
                terrain = "Desert"
            )
        )


        val createdResponse = client.put("http://localhost:${port}/planets/2cec3d8e-0857-4769-b3ad-75069c73d42b") {
            contentType(ContentType.Application.Json)
            setBody(planetToCreate)
        }

        val retrieveCreatedResponse: HttpResponse = client.get("http://localhost:${port}/planets")
        val retrieveCreatedResponseBody: List<Planet> = retrieveCreatedResponse.body()

        val updatedResponse = client.put("http://localhost:${port}/planets/2cec3d8e-0857-4769-b3ad-75069c73d42b") {
            contentType(ContentType.Application.Json)
            setBody(planetToUpdate)
        }

        val retrieveUpdatedResponse: HttpResponse = client.get("http://localhost:${port}/planets")
        val retrieveUpdatedResponseBody: List<Planet> = retrieveUpdatedResponse.body()

        val removeResponse = client.delete("http://localhost:${port}/planets/2cec3d8e-0857-4769-b3ad-75069c73d42b")

        val retrieveRemovedResponse: HttpResponse = client.get("http://localhost:${port}/planets")
        val retrieveRemovedResponseBody: List<Planet> = retrieveRemovedResponse.body()

//        TODO() Retrieve ,update and remove one planet
//

//        Create
        assertThat(createdResponse.status).isEqualTo(HttpStatusCode.NoContent)

//        Retrieve Created
        assertThat(retrieveUpdatedResponse.status).isEqualTo(HttpStatusCode.OK)
        assertThat(retrieveCreatedResponseBody.size).isEqualTo(1)
        assertThat(retrieveCreatedResponseBody).isEqualTo(listCreatedPlanet)

//      Updated
        assertThat(updatedResponse.status).isEqualTo(HttpStatusCode.NoContent)

//      Retrieve Updated
        assertThat(retrieveUpdatedResponse.status).isEqualTo(HttpStatusCode.OK)
        assertThat(retrieveUpdatedResponseBody.size).isEqualTo(1)
        assertThat(retrieveUpdatedResponseBody).isEqualTo(listUpdatedPlanet)

//        Remove
        assertThat(removeResponse.status).isEqualTo(HttpStatusCode.NoContent)


//        Retrieve Remove
        assertThat(retrieveRemovedResponse.status).isEqualTo(HttpStatusCode.OK)
        assertThat(retrieveRemovedResponseBody.size).isEqualTo(0)

    }

    @Test
    fun `can not have 2 planets with same name and id`(){
//        TODO() can not have 2 planets with same name and id
//        TODO tes errors in application

        val planetToCreate1 = Planet(name = "Albania", climate = "tropical", terrain = "tropical forest")
        val planetToCreate2 = Planet(name = "Yavin IV", climate = "Temperate, tropical", terrain = "Jungle, rainforests")
        val planetToCreateWithNameAlreadyExist = Planet(name = "Albania", climate = "arid", terrain = "desert")
        val planetToCreateWithoutClimate = Planet(name = "Tatooine", climate = "", terrain = "desert")
        val planetToCreateWithoutTerrain = Planet(name = "Aladus", climate = "tropical", terrain = "")

        val id1 = "13594cf8-94c6-4ae0-8f6b-dca86e44bbb4"
        val id2 = "2f1a5d4a-a3a2-4f62-9802-2a355c05e09b"
        val id3 = "b6a27d6b-4405-497a-80f4-58d326974ac2"
        val id4 = "3c898c83-f2c3-4ff6-86e3-92f01a978216"
        val id5 = "4a68be6a-39db-4509-8239-35935560db9d"


        val createdResponse1 = runBlocking {
            client.put("http://localhost:${port}/planets/${id1}") {
                contentType(ContentType.Application.Json)
                setBody(planetToCreate1)
            }
        }

        val createdResponse2 = runBlocking {
            client.put("http://localhost:${port}/planets/${id2}") {
                contentType(ContentType.Application.Json)
                setBody(planetToCreate2)
            }
        }

        val createdResponseWithNameAlreadyExist = runBlocking {
            client.put("http://localhost:${port}/planets/${id3}") {
                contentType(ContentType.Application.Json)
                setBody(planetToCreateWithNameAlreadyExist)
            }
        }

        val createdResponseWithoutClimate = runBlocking {
            client.put("http://localhost:${port}/planets/${id4}") {
                contentType(ContentType.Application.Json)
                setBody(planetToCreateWithoutClimate)
            }
        }

        val createdResponseWithoutTerrain = runBlocking {
            client.put("http://localhost:${port}/planets/${id5}") {
                contentType(ContentType.Application.Json)
                setBody(planetToCreateWithoutTerrain)
            }
        }

        val createdResponseWithNameAlreadyExistBody:Response = runBlocking { createdResponseWithNameAlreadyExist.body() }
        val createdResponseWithoutClimateBody:Response = runBlocking { createdResponseWithoutClimate.body() }
        val createdResponseWithoutTerrainBody:Response = runBlocking { createdResponseWithoutTerrain.body() }

        val retrieveResponse: List<Planet>  = runBlocking {
            client.get("http://localhost:${port}/planets").body()
        }

        assertThat(retrieveResponse.size).isEqualTo(2)

        assertThat(createdResponseWithNameAlreadyExist.status).isEqualTo(HttpStatusCode.Conflict)
        assertThat(createdResponseWithNameAlreadyExistBody).isEqualTo(Response(409, "Planet already exists"))

        assertThat(createdResponseWithoutClimate.status).isEqualTo(HttpStatusCode.BadRequest)
        assertThat(createdResponseWithoutClimateBody).isEqualTo(Response(400, "não deve estar vazio"))


        assertThat(createdResponseWithoutTerrain.status).isEqualTo(HttpStatusCode.BadRequest)
        assertThat(createdResponseWithoutTerrainBody).isEqualTo(Response(400, "não deve estar vazio"))

    }


}

//        TODO() make tests with Spring web template and webClient and Retrofit
