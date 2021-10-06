package br.com.quanto.tsp.template.controller.rest.integration

import br.com.quanto.tsp.template.testutils.AbstractKafkaAndPostgresAndVaultTestContainerTest
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.github.tomakehurst.wiremock.matching.AnythingPattern
import com.github.tomakehurst.wiremock.matching.ContainsPattern
import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.test.context.junit.jupiter.EnabledIf
import org.testcontainers.junit.jupiter.Testcontainers
import java.net.URI
import java.util.UUID

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
@EnabledIf(expression = "#{environment.acceptsProfiles('integration')}")
class TemplateHelloWorldControllerIntegrationTest : AbstractKafkaAndPostgresAndVaultTestContainerTest() {

    @LocalServerPort
    var port: Int = 0

    // We are not using Autowired to simulate a external request and do not use sleuth configurations of or own application
    val testRestTemplate: TestRestTemplate = TestRestTemplate()

    @Test
    fun returnInGetHelloWorldMessage() {
        // Stub is in resource/mappings/thirdpartyhelloworldstub.json file
        assertThat(
            testRestTemplate.getForObject(
                "http://localhost:$port/api/v1/hello-world/1",
                String::class.java
            )
        ).contains("Hello World")
    }

    @Test
    fun erro500InGetHelloWorldMessage() {
        // Stub is in resource/mappings/thirdpartyhelloworldstub.json file
        val response = testRestTemplate.getForEntity(
            "http://localhost:$port/api/v1/hello-world/2",
            String::class.java
        )
        Assert.assertEquals(response.statusCode.value(), 500)
    }

    /**
     * Validating the b3 propagation sleuth configs.
     */
    @Test
    fun returnHelloWorldMessage() {
        val xGravitteRequestId = UUID.randomUUID().toString()
        val xB3TraceId = "609e752fa3ad0e4492d52c9e177d8d6c"
        val xB3SpanId = "feced4a32a6d53f9"
        val xB3ParentSpanId = "63b81bc07a1972cd"
        val xB3Sampled = "1"

        // stub is programatically here:
        stubFor(
            post(urlEqualTo("/examples"))
                .withHeader("Content-Type", ContainsPattern(MediaType.APPLICATION_JSON_VALUE))
                .withHeader("X-B3-SpanId", AnythingPattern())
                .withHeader("X-B3-ParentSpanId", AnythingPattern())
                .withHeader("X-B3-Sampled", AnythingPattern())
                .withHeader("X-B3-TraceId", ContainsPattern(xB3TraceId))
                .withHeader("x-gravitee-request-id", ContainsPattern(xGravitteRequestId))
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withBody(
                            "{" +
                                "\"message\": \"Hello World\"," +
                                "\"status\": \"OK\"" +
                                "}"
                        ).withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                )
        )

        val message = UUID.randomUUID().toString()
        val baseUrl = "http://localhost:$port/api/v1/hello-world/$message"
        val uri = URI(baseUrl)

        val headers = HttpHeaders()
        headers.set("X-B3-SpanId", xB3SpanId)
        headers.set("X-B3-ParentSpanId", xB3ParentSpanId)
        headers.set("X-B3-Sampled", xB3Sampled)
        headers.set("X-B3-TraceId", xB3TraceId)
        headers.set("x-gravitee-request-id", xGravitteRequestId)

        val request: HttpEntity<String> = HttpEntity(null, headers)

        val result: ResponseEntity<String> = testRestTemplate.postForEntity(uri, request, String::class.java)

        Assert.assertEquals(200, result.statusCode.value())
    }
}
