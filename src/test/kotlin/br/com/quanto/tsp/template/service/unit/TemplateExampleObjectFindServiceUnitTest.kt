package br.com.quanto.tsp.template.service.unit

import br.com.quanto.tsp.template.model.entity.TemplateExampleObjectEntity
import br.com.quanto.tsp.template.repository.TemplateExampleObjectRepository
import br.com.quanto.tsp.template.service.TemplateExampleObjectFindService
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.Assert
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.UUID

@ExtendWith(MockKExtension::class)
class TemplateExampleObjectFindServiceUnitTest {

    @InjectMockKs
    lateinit var templateExampleObjectFindService: TemplateExampleObjectFindService

    @MockK
    lateinit var templateExampleObjectRepository: TemplateExampleObjectRepository

    @Test
    fun findsOneEntity() {
        val messageMock = UUID.randomUUID().toString()

        val returnObjectMock = TemplateExampleObjectEntity(UUID.randomUUID(), messageMock)

        every { templateExampleObjectRepository.findByMessage(messageMock) } returns returnObjectMock

        val reponse = templateExampleObjectFindService.retrieveExampleObjectByMessage(messageMock)

        Assert.assertEquals(reponse!!.message, messageMock)
        Assert.assertNotNull(reponse.id)
    }

    @Test
    fun doesntFindAnyEntity() {
        val messageMock = UUID.randomUUID().toString()

        every { templateExampleObjectRepository.findByMessage(messageMock) } returns null

        val reponse = templateExampleObjectFindService.retrieveExampleObjectByMessage(messageMock)

        Assert.assertNull(reponse)
    }
}
