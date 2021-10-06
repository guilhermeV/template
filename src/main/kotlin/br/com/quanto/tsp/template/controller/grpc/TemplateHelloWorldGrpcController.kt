package br.com.quanto.tsp.template.controller.grpc

import br.com.quanto.tsp.protobucket.service.template.TemplateGrpc
import br.com.quanto.tsp.protobucket.service.template.TemplateOuterClass
import br.com.quanto.tsp.template.service.TemplateThirdPartyIntegrationGRPCService
import io.grpc.stub.StreamObserver
import mu.KotlinLogging
import net.devh.boot.grpc.server.service.GrpcService

@GrpcService
class TemplateHelloWorldGrpcController(private val templateThirdPartyIntegrationGRPCService: TemplateThirdPartyIntegrationGRPCService) :
    TemplateGrpc.TemplateImplBase() {

    private companion object {
        val log = KotlinLogging.logger {}
    }

    override fun sayHello(
        request: TemplateOuterClass.HelloRequest?,
        responseObserver: StreamObserver<TemplateOuterClass.HelloReply>?,
    ) {
        log.info { "sayHello Request incoming, request=$request" }
        val message = templateThirdPartyIntegrationGRPCService.sendMessageToThirdParty(request!!.name)
        val reply = TemplateOuterClass.HelloReply.newBuilder().setMessage(message).build()
        responseObserver!!.onNext(reply)
        responseObserver.onCompleted()
        log.info { "sayHello Request completed, request=$request, reply=$reply" }
    }
}
