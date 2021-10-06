package br.com.quanto.tsp.template.controller.thirdparty

import br.com.quanto.tsp.protobucket.service.templatethirdparty.TemplatethirdpartyGrpc
import br.com.quanto.tsp.protobucket.service.templatethirdparty.TemplatethirdpartyOuterClass
import io.grpc.stub.StreamObserver
import mu.KotlinLogging
import net.devh.boot.grpc.server.service.GrpcService

@GrpcService
class TemplateThirdPartyGrpcService : TemplatethirdpartyGrpc.TemplatethirdpartyImplBase() {

    private companion object {
        val log = KotlinLogging.logger {}
    }

    override fun sayHello(
        request: TemplatethirdpartyOuterClass.ThirdPartyRequest?,
        responseObserver: StreamObserver<TemplatethirdpartyOuterClass.ThirdPartyReply>?
    ) {
        log.info { "sayHello third party grpc service, request=$request" }
        val reply =
            TemplatethirdpartyOuterClass.ThirdPartyReply.newBuilder().setMessage("HELLO FROM THE OTHER SIDE").build()
        responseObserver!!.onNext(reply)
        responseObserver.onCompleted()
        log.info { "sayHello third party grpc service completed, request=$request, reply=$reply" }
    }
}
