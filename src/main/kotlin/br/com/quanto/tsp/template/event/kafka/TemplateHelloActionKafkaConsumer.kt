package br.com.quanto.tsp.template.event.kafka

import br.com.quanto.tsp.protobucket.action.template.Hello
import br.com.quanto.tsp.template.service.TemplateHelloActionConsumerService
import mu.KotlinLogging
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class TemplateHelloActionKafkaConsumer(private val templateHelloActionConsumerService: TemplateHelloActionConsumerService) {

    companion object {
        const val TOPIC = "example-topic"
        private const val GROUP_ID = "example_group_id"
        val log = KotlinLogging.logger {}
    }

    /**
     * Kafka Consumer Implementation Example...
     */
    @KafkaListener(topics = [TOPIC], groupId = GROUP_ID)
    fun consume(helloAction: Hello.HelloAction) {
        log.info { "Consuming hello action event=$helloAction, topic=$TOPIC, groupId=$GROUP_ID" }
        templateHelloActionConsumerService.handleHelloActionEvent(helloAction)
        log.info { "Consumed hello action event: $helloAction, topic=$TOPIC, groupId=$GROUP_ID" }
    }
}
