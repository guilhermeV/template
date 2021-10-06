package br.com.quanto.tsp.template.repository

import br.com.quanto.tsp.template.model.entity.TemplateExampleObjectEntity
import br.com.quanto.tsp.template.testutils.AbstractKafkaAndPostgresAndVaultTestContainerTest
import io.github.serpro69.kfaker.Faker
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.EnabledIf
import org.springframework.transaction.annotation.Transactional
import org.testcontainers.junit.jupiter.Testcontainers
import java.util.UUID

@Testcontainers
@SpringBootTest
@EnabledIf(expression = "#{environment.acceptsProfiles('integration')}")
class ExampleObjectRepositoryTests : AbstractKafkaAndPostgresAndVaultTestContainerTest() {
    private val faker: Faker = Faker()

    @Autowired
    private lateinit var repository: TemplateExampleObjectRepository

    @Test
    @Transactional
    fun insertsAnEntityAndFindsItById() {
        val entry = TemplateExampleObjectEntity(
            id = UUID.randomUUID(),
            message = faker.lorem.words(),
        )

        repository.insert(entry)

        val templateObject = repository.findById(entry.id)

        templateObject.shouldNotBeNull()
        templateObject.shouldBe(entry)
    }

    @Test
    @Transactional
    fun insertsAnEntityAndFindsItByMessage() {
        val entry = TemplateExampleObjectEntity(
            id = UUID.randomUUID(),
            message = faker.lorem.words(),
        )

        repository.insert(entry)

        val templateObject = repository.findByMessage(entry.message)

        templateObject.shouldNotBeNull()
        templateObject.shouldBe(entry)
    }
}
