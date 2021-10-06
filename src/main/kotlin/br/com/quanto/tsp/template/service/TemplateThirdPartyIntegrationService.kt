package br.com.quanto.tsp.template.service

import br.com.quanto.tsp.template.client.rest.TemplateThirdPartyFeignClient
import br.com.quanto.tsp.template.data.rest.request.TemplateThirdPartyRequest
import br.com.quanto.tsp.template.exception.rest.TemplateUnsuccessCommunicationWithThirdPartyException
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class TemplateThirdPartyIntegrationService(private val templateThirdPartyFeignClient: TemplateThirdPartyFeignClient) {

    private companion object {
        val log = KotlinLogging.logger {}
    }

    fun getMessageFromThirdParty(id: String): String {
        log.info { "Getting message to Third Party service, id=$id" }
        val response = templateThirdPartyFeignClient.makeGetToThirdPartyServer(id)
        if (response.status == "OK") {
            log.info { "Retrieved message to Third Party service with success, id=$id" }
            return response.message
        }
        log.info { "Retrieved message to Third Party service with error, id=$id" }
        throw TemplateUnsuccessCommunicationWithThirdPartyException()
    }

    fun sendMessageToThirdParty(message: String): String {
        log.info { "Sending message to Third Party service, message=$message" }
        val response = templateThirdPartyFeignClient.makePostToThirdPartyServer(TemplateThirdPartyRequest(message))
        if (response.status == "OK") {
            log.info { "Send message to Third Party service with success, message=$message" }
            return response.message
        }
        log.info { "Send message to Third Party service with error, message=$message" }
        throw TemplateUnsuccessCommunicationWithThirdPartyException()
    }
}
