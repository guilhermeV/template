package br.com.quanto.tsp.template.testutils

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent
import org.springframework.context.ApplicationListener
import org.springframework.core.env.PropertiesPropertySource
import java.util.Properties

class EnvironmentListener : ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    override fun onApplicationEvent(event: ApplicationEnvironmentPreparedEvent) {
        val environment = event.environment
        val props = Properties()

        try {
            val cloudVaultUri =
                "http://localhost:${AbstractKafkaAndPostgresAndVaultTestContainerTest.vaultContainer.firstMappedPort}"
            props["spring.cloud.vault.uri"] = cloudVaultUri
            environment.propertySources.addFirst(PropertiesPropertySource("myProps", props))
        } catch (ignored: Exception) {
        }
    }
}
