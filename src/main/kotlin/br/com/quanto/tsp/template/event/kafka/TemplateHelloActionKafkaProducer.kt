package br.com.quanto.tsp.template.event.kafka

import br.com.quanto.tsp.protobucket.action.template.Hello
import mu.KotlinLogging
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class TemplateHelloActionKafkaProducer(private val kafkaTemplate: KafkaTemplate<String, Hello.HelloAction>) {

    private companion object {
        val log = KotlinLogging.logger {}
    }

    /**
     * Kafka Producer with Protobuff event Implementation Example...
     */
    fun sendMessage(message: Hello.HelloAction) {
        log.info { "Sending HelloAction to Kafka, message=$message, topic = ${TemplateHelloActionKafkaConsumer.TOPIC}" }
        kafkaTemplate.send(TemplateHelloActionKafkaConsumer.TOPIC, message)
    }
}
