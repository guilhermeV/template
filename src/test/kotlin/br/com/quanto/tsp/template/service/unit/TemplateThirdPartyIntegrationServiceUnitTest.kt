package br.com.quanto.tsp.template.service.unit

import br.com.quanto.tsp.template.client.rest.TemplateThirdPartyFeignClient
import br.com.quanto.tsp.template.data.rest.request.TemplateThirdPartyRequest
import br.com.quanto.tsp.template.data.rest.response.TemplateThirdPartyResponse
import br.com.quanto.tsp.template.exception.rest.TemplateUnsuccessCommunicationWithThirdPartyException
import br.com.quanto.tsp.template.service.TemplateThirdPartyIntegrationService
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.Assert
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.UUID

@ExtendWith(MockKExtension::class)
class TemplateThirdPartyIntegrationServiceUnitTest {

    @InjectMockKs
    lateinit var templateThirdPartyIntegrationService: TemplateThirdPartyIntegrationService

    @MockK
    lateinit var templateThirdPartyFeignClient: TemplateThirdPartyFeignClient

    @Test
    fun makeFeignGetRequestSuccess() {
        val messageString = UUID.randomUUID().toString()

        val mockResponse = TemplateThirdPartyResponse("OK", messageString)

        every { templateThirdPartyFeignClient.makeGetToThirdPartyServer("1") } returns mockResponse

        val response = templateThirdPartyIntegrationService.getMessageFromThirdParty("1")

        Assert.assertEquals(response, messageString)
    }

    @Test
    fun makeGetFeignRequestThrowException() {
        val messageString = UUID.randomUUID().toString()

        val mockResponse = TemplateThirdPartyResponse("ERROR", messageString)

        every { templateThirdPartyFeignClient.makeGetToThirdPartyServer("1") } returns mockResponse

        Assert.assertThrows(TemplateUnsuccessCommunicationWithThirdPartyException::class.java) {
            templateThirdPartyIntegrationService.getMessageFromThirdParty("1")
        }
    }

    @Test
    fun makeFeignPostRequestSuccess() {
        val messageString = UUID.randomUUID().toString()

        val mockResponse = TemplateThirdPartyResponse("OK", messageString)
        val mockRequest = TemplateThirdPartyRequest(messageString)

        every { templateThirdPartyFeignClient.makePostToThirdPartyServer(mockRequest) } returns mockResponse

        val response = templateThirdPartyIntegrationService.sendMessageToThirdParty(messageString)

        Assert.assertEquals(response, messageString)
    }

    @Test
    fun makePostFeignRequestThrowException() {
        val messageString = UUID.randomUUID().toString()

        val mockResponse = TemplateThirdPartyResponse("ERROR", messageString)
        val mockRequest = TemplateThirdPartyRequest(messageString)

        every { templateThirdPartyFeignClient.makePostToThirdPartyServer(mockRequest) } returns mockResponse

        Assert.assertThrows(TemplateUnsuccessCommunicationWithThirdPartyException::class.java) {
            templateThirdPartyIntegrationService.sendMessageToThirdParty(messageString)
        }
    }
}
