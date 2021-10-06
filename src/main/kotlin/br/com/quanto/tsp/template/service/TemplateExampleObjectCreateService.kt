package br.com.quanto.tsp.template.service

import br.com.quanto.tsp.logpose.exception.LogposeAuditEventProduceException
import br.com.quanto.tsp.logpose.service.LogposeAuditEventProducerService
import br.com.quanto.tsp.protobucket.event.logpose.AuditLogEventOuterClass.DataOwner
import br.com.quanto.tsp.protobucket.event.logpose.AuditLogEventOuterClass.Rut
import br.com.quanto.tsp.template.model.entity.TemplateExampleObjectEntity
import br.com.quanto.tsp.template.repository.TemplateExampleObjectRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class TemplateExampleObjectCreateService(
    private val logposeAuditEventProducerService: LogposeAuditEventProducerService,
    private val templateExampleObjectRepository: TemplateExampleObjectRepository,
) {

    companion object {
        private val logger = LoggerFactory.getLogger(TemplateExampleObjectCreateService::class.java)
    }

    fun createExampleObjectFromMessage(message: String): TemplateExampleObjectEntity {
        val newObjectToPersist = TemplateExampleObjectEntity(UUID.randomUUID(), message)

        templateExampleObjectRepository.insert(newObjectToPersist)

        try {
            logposeAuditEventProducerService.auditInfo(
                "ACTION_UNKNOW",
                "Created Example Object From Message",
                Rut.newBuilder()
                    .setConsentId(UUID.randomUUID().toString())
                    .setReceiverId(UUID.randomUUID().toString())
                    .setDataOwner(
                        DataOwner.newBuilder()
                            .setUserId(UUID.randomUUID().toString())
                            .build()
                    ).build(),
                mutableMapOf("message" to message)
            )
        } catch (logposeException: LogposeAuditEventProduceException) {
            handleAuditException(newObjectToPersist)
            throw RuntimeException("ERROR AUDITING EVENT")
        }
        return newObjectToPersist
    }

    private fun handleAuditException(templateExampleObject: TemplateExampleObjectEntity) =
        logger.info("handleAuditException:" + templateExampleObject.message)
}
