package br.com.quanto.tsp.template.model.database

import org.jetbrains.exposed.sql.Table

class TemplateDatabase {

    object TemplateExampleObjectTable : Table("example_object") {
        val id = uuid("eo_id")
        val message = text("message").nullable()
        override val primaryKey = PrimaryKey(id)
    }
}
