package br.com.quanto.tsp.template.service.unit

import br.com.quanto.tsp.logpose.exception.LogposeAuditEventProduceException
import br.com.quanto.tsp.logpose.service.LogposeAuditEventProducerService
import br.com.quanto.tsp.protobucket.event.logpose.AuditLogEventOuterClass
import br.com.quanto.tsp.template.model.entity.TemplateExampleObjectEntity
import br.com.quanto.tsp.template.repository.TemplateExampleObjectRepository
import br.com.quanto.tsp.template.service.TemplateExampleObjectCreateService
import br.com.quanto.tsp.template.testutils.TemplateAbstractUnitTest
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.Assert
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.UUID

@ExtendWith(MockKExtension::class)
class TemplateExampleObjectCreateServiceUnitTest : TemplateAbstractUnitTest() {

    @InjectMockKs
    lateinit var templateExampleObjectCreateService: TemplateExampleObjectCreateService

    @RelaxedMockK
    lateinit var templateExampleObjectRepository: TemplateExampleObjectRepository

    @RelaxedMockK
    lateinit var logposeAuditEventProducerService: LogposeAuditEventProducerService

    @Test
    fun createExampleObjectEntity() {

        val messageMock = UUID.randomUUID().toString()

        val response = templateExampleObjectCreateService.createExampleObjectFromMessage(messageMock)

        verify(exactly = 1) { templateExampleObjectRepository.insert(ofType(TemplateExampleObjectEntity::class)) }

        Assert.assertNotNull(response.id)
        Assert.assertEquals(response.message, messageMock)
    }

    @Test
    fun throwExceptionExampleObjectEntityAuditEventError() {
        val messageMock = UUID.randomUUID().toString()

        every {
            logposeAuditEventProducerService.auditInfo(
                ofType(String::class),
                ofType(String::class),
                ofType(AuditLogEventOuterClass.Rut::class),
                any()
            )
        } throws LogposeAuditEventProduceException(RuntimeException("AuditError"))

        Assertions.assertThrows(RuntimeException::class.java) {
            templateExampleObjectCreateService.createExampleObjectFromMessage(messageMock)
        }
    }
}
