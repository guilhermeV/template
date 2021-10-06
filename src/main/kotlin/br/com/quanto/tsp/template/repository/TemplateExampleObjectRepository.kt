package br.com.quanto.tsp.template.repository

import br.com.quanto.tsp.template.model.database.TemplateDatabase.TemplateExampleObjectTable
import br.com.quanto.tsp.template.model.entity.TemplateExampleObjectEntity
import mu.KotlinLogging
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Repository
@Transactional
class TemplateExampleObjectRepository {

    private companion object {
        val log = KotlinLogging.logger {}
    }

    fun insert(entry: TemplateExampleObjectEntity): UUID {
        log.info { "Inserting in TemplateExampleObject table, entry=$entry" }
        return TemplateExampleObjectTable.insert {
            it[id] = entry.id
            it[message] = entry.message
        } get TemplateExampleObjectTable.id
    }

    fun findById(id: UUID): TemplateExampleObjectEntity? {
        log.info { "Finding in TemplateExampleObject table, id=$id" }
        return TemplateExampleObjectTable.select { TemplateExampleObjectTable.id eq id }
            .map { fromRow(it) }
            .firstOrNull()
    }

    fun findByMessage(message: String?): TemplateExampleObjectEntity? {
        log.info { "Finding in TemplateExampleObject table, message=$message" }
        return TemplateExampleObjectTable.select { TemplateExampleObjectTable.message eq message }
            .map { fromRow(it) }
            .firstOrNull()
    }

    private fun fromRow(r: ResultRow) =
        TemplateExampleObjectEntity(
            r[TemplateExampleObjectTable.id],
            r[TemplateExampleObjectTable.message]
        )
}
