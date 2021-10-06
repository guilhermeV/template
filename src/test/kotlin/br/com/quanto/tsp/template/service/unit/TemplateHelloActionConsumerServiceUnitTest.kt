package br.com.quanto.tsp.template.service.unit

import br.com.quanto.tsp.protobucket.action.template.Hello
import br.com.quanto.tsp.template.model.entity.TemplateExampleObjectEntity
import br.com.quanto.tsp.template.service.TemplateExampleObjectCreateService
import br.com.quanto.tsp.template.service.TemplateHelloActionConsumerService
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.Assert
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.UUID

@ExtendWith(MockKExtension::class)
class TemplateHelloActionConsumerServiceUnitTest {

    @InjectMockKs
    lateinit var templateHelloActionConsumerService: TemplateHelloActionConsumerService

    @MockK
    lateinit var templateExampleObjectCreateService: TemplateExampleObjectCreateService

    @Test
    fun handleActionEvent() {

        val mockMessage = UUID.randomUUID().toString()
        val helloActionEvent = Hello.HelloAction.newBuilder()
            .setMessage(mockMessage)
            .build()

        val objectMock = TemplateExampleObjectEntity(UUID.randomUUID(), mockMessage)

        every { templateExampleObjectCreateService.createExampleObjectFromMessage(mockMessage) } returns objectMock

        templateHelloActionConsumerService.handleHelloActionEvent(helloActionEvent)
    }

    @Test
    fun handleActionEventThrowMessage() {

        val mockMessage = UUID.randomUUID().toString()
        val helloActionEvent = Hello.HelloAction.newBuilder()
            .setMessage(mockMessage)
            .build()

        val objectMock = TemplateExampleObjectEntity(UUID.randomUUID(), null)

        every { templateExampleObjectCreateService.createExampleObjectFromMessage(mockMessage) } returns objectMock

        Assert.assertThrows(RuntimeException::class.java) {
            templateHelloActionConsumerService.handleHelloActionEvent(helloActionEvent)
        }
    }
}
