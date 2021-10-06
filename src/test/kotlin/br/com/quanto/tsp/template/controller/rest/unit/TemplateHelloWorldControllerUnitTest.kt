package br.com.quanto.tsp.template.controller.rest.unit

import br.com.quanto.tsp.template.controller.rest.TemplateHelloWorldController
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
class TemplateHelloWorldControllerUnitTest {

    @InjectMockKs
    lateinit var templateHelloWorldController: TemplateHelloWorldController

    @MockK
    lateinit var templateThirdPartyIntegrationService: TemplateThirdPartyIntegrationService

    private val helloWorldMessage = "Hello Word"

    @Test
    fun returnHelloWorldMessageGet() {
        every { templateThirdPartyIntegrationService.getMessageFromThirdParty("1") } returns helloWorldMessage

        val returnHello = templateHelloWorldController.sayHelloGet("1")

        Assert.assertEquals(returnHello, helloWorldMessage)
    }

    @Test
    fun throwsExceptionGet() {
        every { templateThirdPartyIntegrationService.getMessageFromThirdParty("2") } throws TemplateUnsuccessCommunicationWithThirdPartyException()

        Assert.assertThrows(TemplateUnsuccessCommunicationWithThirdPartyException::class.java) {
            templateHelloWorldController.sayHelloGet("2")
        }
    }

    @Test
    fun returnHelloWorldMessagePost() {
        val msg = UUID.randomUUID().toString()

        every { templateThirdPartyIntegrationService.sendMessageToThirdParty(msg) } returns helloWorldMessage

        val returnHello = templateHelloWorldController.sayHello(msg)

        Assert.assertEquals(returnHello, helloWorldMessage)
    }

    @Test
    fun throwsExceptionInPost() {
        val msg = UUID.randomUUID().toString()

        every { templateThirdPartyIntegrationService.sendMessageToThirdParty(msg) } throws TemplateUnsuccessCommunicationWithThirdPartyException()

        Assert.assertThrows(TemplateUnsuccessCommunicationWithThirdPartyException::class.java) {
            templateHelloWorldController.sayHello(msg)
        }
    }
}
