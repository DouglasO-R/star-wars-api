package oliveira.technological.solutions.controller

import oliveira.technological.solutions.model.Planet
import oliveira.technological.solutions.repository.PlanetRepository
import org.junit.jupiter.api.Assertions
import org.mockito.Mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import kotlin.test.Test


@SpringBootTest
@AutoConfigureMockMvc
class PlanetsControllerTest {

    @Mock
    lateinit var planetRepository: PlanetRepository

    @Autowired
    val mockMvc: MockMvc? = null

    @Test
    fun `should create a planet`() {
        val tatooine = Planet("d06891dc-2a05-4aba-a7c6-389220191b66", "Tatooine", "Arid", "Desert")
        val json = """
                { 
                    "name": "Tatooine",
                    "climate": "Arid",
                    "terrain":"Desert"
                }
                """.trimIndent()

        val id = "d06891dc-2a05-4aba-a7c6-389220191b66"
        //ACT
        val response = mockMvc!!.perform(
            put("/planets/${id}")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().response


        //ASSERT
        Assertions.assertEquals(204, response.status)
    }

    @Test
    fun `should return created planet`() {
        val tatooine = Planet("d06891dc-2a05-4aba-a7c6-389220191b66", "Tatooine", "Arid", "Desert")
        val createJson = """
                { 
                    "name": "Tatooine",
                    "climate": "Arid",
                    "terrain":"Desert"
                }
                """.trimIndent()

        val id = "d06891dc-2a05-4aba-a7c6-389220191b66"

        mockMvc!!.perform(
            put("/planets/${id}")
                .content(createJson)
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().response

        val resultJson = """
                { 
                    "id" : ""d06891dc-2a05-4aba-a7c6-389220191b66"",
                    "name": "Tatooine",
                    "climate": "Arid",
                    "terrain":"Desert"
                }
                """.trimIndent()


        val expectedResult = listOf(resultJson)
        //ACT
        val response = mockMvc!!.perform(
            get("/planets")
        ).andReturn().response


        //ASSERT
        Assertions.assertEquals(200, response.status)
//        Assertions.assertEquals(expectedResult, response.contentAsString)
    }
    @Test
    fun `should create a planet and remove planet`() {
        val tatooine = Planet("d06891dc-2a05-4aba-a7c6-389220191b66", "Tatooine", "Arid", "Desert")
        val createJson = """
                { 
                    "name": "Tatooine",
                    "climate": "Arid",
                    "terrain":"Desert"
                }
                """.trimIndent()

        val id = "d06891dc-2a05-4aba-a7c6-389220191b66"

        mockMvc!!.perform(
            put("/planets/${id}")
                .content(createJson)
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().response


        //ACT
        val response = mockMvc!!.perform(
            delete("/planets/${id}")
        ).andReturn().response


        //ASSERT
        Assertions.assertEquals(204, response.status)
//        Assertions.assertEquals(expectedResult, response.contentAsString)
    }
    @Test
    fun `should search by id and return status 200`() {

        val tatooine = Planet("d06891dc-2a05-4aba-a7c6-389220191b66", "Tatooine", "Arid", "Desert")
        val createJson = """
                { 
                    "name": "Tatooine",
                    "climate": "Arid",
                    "terrain":"Desert"
                }
                """.trimIndent()

        val id = "d06891dc-2a05-4aba-a7c6-389220191b66"

        mockMvc!!.perform(
            put("/planets/${id}")
                .content(createJson)
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().response

        val resultJson = """
                { 
                    "id" : ""d06891dc-2a05-4aba-a7c6-389220191b66"",
                    "name": "Tatooine",
                    "climate": "Arid",
                    "terrain":"Desert"
                }
                """.trimIndent()


        //ACT
        val response = mockMvc!!.perform(
            get("/planets/${id}")
        ).andReturn().response


        //ASSERT
        Assertions.assertEquals(200, response.status)
//        Assertions.assertEquals(expectedResult, response.contentAsString)
    }

}