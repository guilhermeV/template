package br.com.quanto.tsp.template.controller.grpc.unit

import br.com.quanto.tsp.protobucket.service.template.TemplateOuterClass.HelloReply
import br.com.quanto.tsp.protobucket.service.template.TemplateOuterClass.HelloRequest
import br.com.quanto.tsp.template.controller.grpc.TemplateHelloWorldGrpcController
import br.com.quanto.tsp.template.service.TemplateThirdPartyIntegrationGRPCService
import io.grpc.internal.testing.StreamRecorder
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.fail
import java.util.concurrent.TimeUnit

@ExtendWith(MockKExtension::class)
class TemplateHelloWorldGrpcControllerUnitTest {

    @InjectMockKs
    lateinit var templateHelloWorldGrpcController: TemplateHelloWorldGrpcController

    @MockK
    lateinit var templateThirdPartyIntegrationGRPCService: TemplateThirdPartyIntegrationGRPCService

    @Test
    fun testSayHello() {
        val messageString = "Hello ==> Test"

        every { templateThirdPartyIntegrationGRPCService.sendMessageToThirdParty("Test") } returns messageString

        val request = HelloRequest.newBuilder()
            .setName("Test")
            .build()

        val responseObserver: StreamRecorder<HelloReply> = StreamRecorder.create()

        templateHelloWorldGrpcController.sayHello(request, responseObserver)
        if (!responseObserver.awaitCompletion(5, TimeUnit.SECONDS)) {
            fail("The call did not terminate in time")
        }
        assertNull(responseObserver.error)
        val results: List<HelloReply> = responseObserver.values
        assertEquals(1, results.size)
        val response = results[0]
        assertEquals(HelloReply.newBuilder().setMessage(messageString).build(), response)
    }
}
