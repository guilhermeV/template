package br.com.quanto.tsp.template.event.kafka

import br.com.quanto.tsp.protobucket.action.template.Hello
import br.com.quanto.tsp.template.service.TemplateHelloActionConsumerService
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class TemplateHelloActionKafkaConsumerTest {

    @InjectMockKs
    lateinit var templateHelloActionKafkaConsumer: TemplateHelloActionKafkaConsumer

    @RelaxedMockK
    lateinit var templateHelloActionConsumerService: TemplateHelloActionConsumerService

    @Test
    fun consumeOneEvent() {

        val messageString = ObjectIdGenerators.UUIDGenerator().generateId(this).toString()

        val action = Hello.HelloAction.newBuilder().setMessage(messageString).build()

        templateHelloActionKafkaConsumer.consume(action)

        verify(exactly = 1) { templateHelloActionConsumerService.handleHelloActionEvent(action) }
    }
}
