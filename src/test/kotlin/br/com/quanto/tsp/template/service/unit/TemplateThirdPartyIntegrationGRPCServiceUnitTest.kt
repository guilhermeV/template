package br.com.quanto.tsp.template.service.unit

import br.com.quanto.tsp.protobucket.service.templatethirdparty.TemplatethirdpartyOuterClass
import br.com.quanto.tsp.template.client.grpc.TemplateThirdPartyGrpcClient
import br.com.quanto.tsp.template.service.TemplateThirdPartyIntegrationGRPCService
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.Assert
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class TemplateThirdPartyIntegrationGRPCServiceUnitTest {

    @InjectMockKs
    lateinit var templateThirdPartyIntegrationGRPCService: TemplateThirdPartyIntegrationGRPCService

    @MockK
    lateinit var templateThirdPartyGrpcClient: TemplateThirdPartyGrpcClient

    @Test
    fun makeRequest() {
        val messageString = "Test"

        val replyMock = TemplatethirdpartyOuterClass.ThirdPartyReply.newBuilder().setMessage(messageString).build()
        val requestMock = TemplatethirdpartyOuterClass.ThirdPartyRequest.newBuilder().setName(messageString).build()
        every { templateThirdPartyGrpcClient.communicate(requestMock) } returns replyMock

        val message = templateThirdPartyIntegrationGRPCService.sendMessageToThirdParty(messageString)

        Assert.assertEquals(message, messageString)
    }
}
