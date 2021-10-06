package br.com.quanto.tsp.template.config.kafka

import br.com.quanto.tsp.protobucket.action.template.Hello
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory

@Configuration
class TemplateKafkaProducerConfig {

    /**
     * Kafka Protobuff Producert Example...
     */
    @Bean
    @Primary
    fun kafkaTemplate(producerFactory: ProducerFactory<String, Hello.HelloAction>) = KafkaTemplate(producerFactory)
}
