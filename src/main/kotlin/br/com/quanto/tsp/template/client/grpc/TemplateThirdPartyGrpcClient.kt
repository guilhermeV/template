package br.com.quanto.tsp.template.client.grpc

import br.com.quanto.tsp.protobucket.service.templatethirdparty.TemplatethirdpartyGrpc
import br.com.quanto.tsp.protobucket.service.templatethirdparty.TemplatethirdpartyOuterClass.ThirdPartyReply
import br.com.quanto.tsp.protobucket.service.templatethirdparty.TemplatethirdpartyOuterClass.ThirdPartyRequest
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.stereotype.Component

@Component
class TemplateThirdPartyGrpcClient {

    @GrpcClient("third-party-server")
    lateinit var templatethirdpartyBlockingStub: TemplatethirdpartyGrpc.TemplatethirdpartyBlockingStub

    fun communicate(request: ThirdPartyRequest): ThirdPartyReply {
        return templatethirdpartyBlockingStub.sayHello(request)
    }
}
