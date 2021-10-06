package br.com.quanto.tsp.template.controller.grpc.integration

import br.com.quanto.tsp.protobucket.service.template.TemplateGrpc
import br.com.quanto.tsp.protobucket.service.template.TemplateOuterClass
import br.com.quanto.tsp.protobucket.service.templatethirdparty.TemplatethirdpartyGrpc
import br.com.quanto.tsp.protobucket.service.templatethirdparty.TemplatethirdpartyOuterClass
import br.com.quanto.tsp.protobucket.service.templatethirdparty.TemplatethirdpartyOuterClass.ThirdPartyReply
import br.com.quanto.tsp.template.testutils.AbstractKafkaAndPostgresAndVaultTestContainerTest
import io.github.serpro69.kfaker.Faker
import net.devh.boot.grpc.client.inject.GrpcClient
import org.grpcmock.GrpcMock.stubFor
import org.grpcmock.GrpcMock.unaryMethod
import org.grpcmock.springboot.AutoConfigureGrpcMock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.EnabledIf
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest(
    properties = [
        "grpc.server.inProcessName=helloworld",
        "grpc.server.port=-1",
        "grpc.client.helloWorld.address=in-process:helloworld"
    ]
)
@Testcontainers
@AutoConfigureGrpcMock
@EnabledIf(expression = "#{environment.acceptsProfiles('integration')}")
class TemplateHelloWorldGrpcControllerIntegrationTestAndVault : AbstractKafkaAndPostgresAndVaultTestContainerTest() {

    @GrpcClient("helloWorld")
    lateinit var helloWorldBlockingStub: TemplateGrpc.TemplateBlockingStub

    private var faker = Faker()

    @Test
    fun testSayHello() {

        val message = faker.lorem.words()
        val mockResponseMessage = "MOCKRESPONSE$message"

        val responseMock = ThirdPartyReply.newBuilder()
            .setMessage(mockResponseMessage).build()

        val expectedRequest = TemplatethirdpartyOuterClass.ThirdPartyRequest.newBuilder()
            .setName(message).build()

        // mock third party grpc server request
        stubFor(
            unaryMethod(TemplatethirdpartyGrpc.getSayHelloMethod())
                .withRequest(expectedRequest)
                .willReturn(responseMock)
        )

        val request = TemplateOuterClass.HelloRequest.newBuilder().setName(message).build()

        val response = helloWorldBlockingStub.sayHello(request)
        assertNotNull(response)
        assertEquals(mockResponseMessage, response.message)
    }
}
