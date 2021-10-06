package br.com.quanto.tsp.template.controller.rest

import br.com.quanto.tsp.template.service.TemplateThirdPartyIntegrationService
import mu.KotlinLogging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/hello-world")
class TemplateHelloWorldController(private val templateThirdPartyIntegrationService: TemplateThirdPartyIntegrationService) {

    private companion object {
        val log = KotlinLogging.logger {}
    }

    @GetMapping("/{id}")
    fun sayHelloGet(@PathVariable("id") id: String): String {
        log.info { "sayHelloGet received, id=$id" }
        return templateThirdPartyIntegrationService.getMessageFromThirdParty(id)
    }

    @PostMapping("/{message}")
    fun sayHello(
        @PathVariable("message") message: String,
    ): String {
        log.info { "sayHello send message, message=$message" }
        return templateThirdPartyIntegrationService.sendMessageToThirdParty(message)
    }
}
