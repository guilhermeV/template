package br.com.quanto.tsp.template.service.unit

import br.com.quanto.tsp.protobucket.action.template.Hello
import br.com.quanto.tsp.template.event.kafka.TemplateHelloActionKafkaProducer
import br.com.quanto.tsp.template.service.TemplateEventProducerService
import io.github.serpro69.kfaker.Faker
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class TemplateEventProducerServiceUnitTests {

    @InjectMockKs
    lateinit var templateEventProducerService: TemplateEventProducerService

    @RelaxedMockK
    lateinit var templateHelloActionKafkaProducer: TemplateHelloActionKafkaProducer

    private var faker = Faker()

    @Test
    fun produceEvent() {
        val messageString = faker.lorem.words()

        val action = Hello.HelloAction.newBuilder().setMessage(messageString).build()
        templateEventProducerService.sendMessage(messageString)

        verify(exactly = 1) { templateHelloActionKafkaProducer.sendMessage(action) }
    }
}
