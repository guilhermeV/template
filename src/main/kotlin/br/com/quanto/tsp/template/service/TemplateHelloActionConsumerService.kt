package br.com.quanto.tsp.template.service

import br.com.quanto.tsp.protobucket.action.template.Hello
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class TemplateHelloActionConsumerService(private val templateExampleObjectCreateService: TemplateExampleObjectCreateService) {

    private companion object {
        val log = KotlinLogging.logger {}
    }

    fun handleHelloActionEvent(helloAction: Hello.HelloAction) {
        val objectCreated = templateExampleObjectCreateService.createExampleObjectFromMessage(helloAction.message)
        if (objectCreated.message == null) {
            log.info { "Consumed hello action event with error: $helloAction" }
            throw RuntimeException()
        }
    }
}
