package oliveira.technological.solutions.controller

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import oliveira.technological.solutions.model.Planet
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import kotlin.test.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class PlanetsControllerMockMvc {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Test
    fun `should Create A Planet`(): Unit {
        val planetToCreate = Planet(name = "Albania", climate = "tropical", terrain = "tropical forest")
        val jsonPlanet = objectMapper.writeValueAsString(planetToCreate)


         mockMvc.perform(put("/planets/2cec3d8e-0857-4769-b3ad-75069c73d42b")
             .contentType(MediaType.APPLICATION_JSON)
             .content(jsonPlanet)
         ).andDo(print()).andExpect(status().isNoContent)



    }
}