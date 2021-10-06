package br.com.quanto.tsp.template.config.kafka

import br.com.quanto.tsp.protobucket.action.template.Hello.HelloAction
import com.github.daniel.shuy.kafka.protobuf.serde.KafkaProtobufDeserializer
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.config.KafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer

@Configuration
class TemplateKafkaConsumerConfig {

    @Autowired
    lateinit var kafkaProperties: KafkaProperties

    /**
     * Kafka Protobuff Consumer Example...
     */
    @Bean
    fun kafkaListenerContainerFactory(): KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String?, HelloAction?>>? {
        val factory = ConcurrentKafkaListenerContainerFactory<String, HelloAction>()
        factory.consumerFactory = consumerFactory()
        return factory
    }

    /**
     * Kafka Protobuff Consumer Factory Example...
     */
    @Bean
    fun consumerFactory(): ConsumerFactory<String?, HelloAction?> {
        return DefaultKafkaConsumerFactory(
            kafkaProperties.buildConsumerProperties(),
            StringDeserializer(),
            KafkaProtobufDeserializer(HelloAction.parser())
        )
    }
}
