package br.com.quanto.tsp.template.event.kafka

import br.com.quanto.tsp.protobucket.action.template.Hello
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.kafka.core.KafkaTemplate

@ExtendWith(MockKExtension::class)
class TemplateActionEventKafkaProducerTest {

    @InjectMockKs
    lateinit var templateHelloActionKafkaProducer: TemplateHelloActionKafkaProducer

    @RelaxedMockK
    lateinit var kafkaTemplate: KafkaTemplate<String, Hello.HelloAction>

    @Test
    fun produceOneEvent() {

        val messageString = ObjectIdGenerators.UUIDGenerator().generateId(this).toString()

        val action = Hello.HelloAction.newBuilder().setMessage(messageString).build()

        templateHelloActionKafkaProducer.sendMessage(action)

        verify(exactly = 1) { kafkaTemplate.send("example-topic", action) }
    }
}
