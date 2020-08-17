package software.jumbo.storesfinder

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.geo.GeoJsonModule
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import org.springframework.test.web.reactive.server.returnResult
import software.jumbo.storesfinder.StoresFixtures.newRawStore552211
import software.jumbo.storesfinder.StoresFixtures.parsedDocStore3178Success
import software.jumbo.storesfinder.StoresFixtures.parsedDocStore3501
import software.jumbo.storesfinder.StoresFixtures.rawExistentStore3178
import software.jumbo.storesfinder.StoresFixtures.rawExistentStore3178Change
import software.jumbo.storesfinder.domain.StoreDoc


@SpringBootTest
@AutoConfigureWebTestClient
@Disabled
class StoresFinderIntegrationTest(
    @Autowired val client: WebTestClient
) {

    @Test
    fun `Test expected return for controller get api stores`() {
        GeoJsonModule()
        client.get().uri("/api/stores/3178")
            .exchange()
            .expectStatus().isOk
            .returnResult<StoreDoc>()
    }


    @Test
    fun `Test expected return for controller post api stores`() {
        client.post().uri("/api/stores/")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(rawExistentStore3178)
            .exchange()
            .expectStatus().isOk
            .expectBody<String>()
            .isEqualTo(parsedDocStore3178Success)
    }


    @Test
    fun `Retrieve parsed store 3501 through endpoint`() {
        client.get().uri("/api/stores/3501").exchange()
            .expectStatus().isOk
            .expectBody<String>()
            .isEqualTo(parsedDocStore3501)
    }

    @Test
    fun `Attempt to fetch a not existent store`() {
        client.get().uri("/api/stores/317899").exchange()
            .expectStatus().isOk
    }

    @Test
    fun `Add a new store on via stores endpoint`() {
        client.post().uri("/api/stores/")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(newRawStore552211)
            .exchange()
            .expectStatus().isOk
            .expectBody<String>()
            .isEqualTo("""{"id": "552211", "name": "Jumbo New Added"}""")
    }

    @Test
    fun `Update a store on via stores endpoint`() {
        client.post().uri("/api/stores/")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(rawExistentStore3178Change)
            .exchange()
            .expectStatus().isOk
            .expectBody<String>()
            .isEqualTo("""{"id": "3178", "name": "Jumbo Change1"}""")
    }

    @Test
    fun `Search nearby stores close to lat xx and long yy`() {
        client.get()
            .uri("/api/stores/nearby/4.7691187664710455/52.86885159032157")
            .exchange()
            .expectStatus().isOk
            .expectBody<String>()
            .consumeWith { resp ->
                Assertions.assertTrue(resp.responseBody?.contains("Jumbo Anna Paulowna Molengang") ?: false)
            }
    }

    @Test
    fun `Search nearby stores close to 2000 random positions`() {
        val mapper = jacksonObjectMapper()
        client.get()
            .uri("/api/stores/nearby/5.541820/51.616119")
            .exchange()
            .expectStatus().isOk
            .expectBody<String>()
            .consumeWith { resp ->
                val arrayReturn: Array<*> = mapper.readValue<Array<Any>>(resp.responseBody.orEmpty())
                Assertions.assertEquals(5, arrayReturn.size)
            }

    }


}


