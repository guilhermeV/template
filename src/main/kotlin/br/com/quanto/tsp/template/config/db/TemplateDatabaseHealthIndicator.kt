package br.com.quanto.tsp.template.config.db

import org.springframework.boot.actuate.health.Health
import org.springframework.boot.actuate.health.HealthIndicator
import org.springframework.boot.actuate.jdbc.DataSourceHealthIndicator
import org.springframework.stereotype.Component
import javax.sql.DataSource

@Component
class TemplateDatabaseHealthIndicator(private val datasource: DataSource) : HealthIndicator {

    override fun health(): Health {
        return DataSourceHealthIndicator(datasource, "select 1").health()
    }
}
