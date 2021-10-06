package br.com.quanto.tsp.template.service

import br.com.quanto.tsp.protobucket.action.template.Hello.HelloAction
import br.com.quanto.tsp.template.event.kafka.TemplateHelloActionKafkaProducer
import org.springframework.stereotype.Service

@Service
class TemplateEventProducerService(private val templateHelloActionKafkaProducer: TemplateHelloActionKafkaProducer) {

    fun sendMessage(message: String) {
        templateHelloActionKafkaProducer.sendMessage(HelloAction.newBuilder().setMessage(message).build())
    }
}
