package br.com.quanto.tsp.template.service

import br.com.quanto.tsp.protobucket.service.templatethirdparty.TemplatethirdpartyOuterClass
import br.com.quanto.tsp.template.client.grpc.TemplateThirdPartyGrpcClient
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class TemplateThirdPartyIntegrationGRPCService(private val templateThirdPartyGrpcClient: TemplateThirdPartyGrpcClient) {

    private companion object {
        val log = KotlinLogging.logger {}
    }

    fun sendMessageToThirdParty(message: String): String {
        log.info { "Sending message to Third Party service, message=$message" }
        val request = TemplatethirdpartyOuterClass.ThirdPartyRequest.newBuilder().setName(message).build()
        val communicateReply = templateThirdPartyGrpcClient.communicate(request)
        log.info { "Send message to Third Party service, message=$message" }
        return communicateReply.message
    }
}
