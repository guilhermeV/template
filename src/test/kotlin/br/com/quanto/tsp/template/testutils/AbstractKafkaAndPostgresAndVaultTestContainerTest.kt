package br.com.quanto.tsp.template.testutils

import br.com.quanto.tsp.template.event.kafka.TemplateHelloActionKafkaConsumer
import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.containers.Network
import org.testcontainers.utility.DockerImageName

abstract class AbstractKafkaAndPostgresAndVaultTestContainerTest {

    companion object {

        var network: Network = Network.newNetwork()

        var postgresContainer: QuantoPostgreSQLContainer = QuantoPostgreSQLContainer("postgres:13.2")
            .withDatabaseName("template")
            .withUsername("templateuser")
            .withPassword("templatepassword")
            .withNetwork(network)
            .withNetworkAliases("postgres")

        var vaultContainer: QuantoVaultContainer = QuantoVaultContainer("vault:1.7.3")
            .withVaultToken("token123")
            .withNetwork(network)
            .withNetworkAliases("vault")

        var kafkaContainer: KafkaContainer = KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:5.4.3"))

        @JvmStatic
        @DynamicPropertySource
        fun properties(
            registry: DynamicPropertyRegistry,
        ) {
            registry.add("spring.kafka.consumer.bootstrap-servers", kafkaContainer::getBootstrapServers)
            registry.add("spring.kafka.producer.bootstrap-servers", kafkaContainer::getBootstrapServers)
            registry.add("spring.datasource.url", postgresContainer::getJdbcUrl)
            registry.add("quanto.audit.bootstrapserver", kafkaContainer::getBootstrapServers)
        }
    }

    init {
        postgresContainer.start()
        vaultContainer.start()

        // Kv secret Engine
        vaultContainer.execInContainer("vault", "secrets", "disable", "secret")
        vaultContainer.execInContainer("vault", "secrets", "enable", "-version=1", "-path=secret", "kv")

        vaultContainer.execInContainer(
            "vault",
            "kv",
            "put",
            "secret/template",
            "example.username=demouser",
            "example.password=demopassword"
        )

        // please ensure client's policies grant access to path \\\"database/creds/order/\\
        // policies
        val policies = "'path \"secret/template*\" {\n" +
            "  capabilities = [\"create\", \"read\", \"update\", \"delete\", \"list\"]\n" +
            "}\n" +
            "path \"secret/application*\" {\n" +
            "  capabilities = [\"create\", \"read\", \"update\", \"delete\", \"list\"]\n" +
            "}\n" +
            "path \"transit/decrypt/order\" {\n" +
            "  capabilities = [\"update\"]\n" +
            "}\n" +
            "path \"transit/encrypt/order\" {\n" +
            "  capabilities = [\"update\"]\n" +
            "}\n" +
            "path \"database/creds/order\" {\n" +
            "  capabilities = [\"read\"]\n" +
            "}\n" +
            "path \"sys/renew/*\" {\n" +
            "  capabilities = [\"update\"]\n" +
            "}'"
        vaultContainer.execInContainer("echo", policies, "|", "vault", "policy", "write", "order", "-")

        // Mount DB backend
        vaultContainer.execInContainer("vault", "secrets", "enable", "database")

        // Create the DB connection
        vaultContainer.execInContainer(
            "vault",
            "write",
            "database/config/template",
            "plugin_name=postgresql-database-plugin",
            "allowed_roles=*",
            "connection_url=postgresql://{{username}}:{{password}}@postgres:5432/template?sslmode=disable",
            "username=${postgresContainer.username}",
            "password=${postgresContainer.password}"
        )

        // Create the DB order role
        vaultContainer.execInContainer(
            "vault",
            "write",
            "database/roles/order",
            "db_name=${postgresContainer.databaseName}",
            "creation_statements=CREATE ROLE \"{{name}}\" WITH LOGIN PASSWORD '{{password}}' VALID UNTIL '{{expiration}}'; GRANT USAGE ON ALL SEQUENCES IN SCHEMA public TO \"{{name}}\"; GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO \"{{name}}\";",
            "default_ttl=1h",
            "max_ttl=24h"
        )

        // Transit Config
        vaultContainer.execInContainer(
            "vault",
            "secrets",
            "enable",
            "transit"
        )

        vaultContainer.execInContainer(
            "vault",
            "write",
            "-f",
            "transit/keys/order"
        )

        kafkaContainer.start()

        val adminClient: AdminClient = AdminClient.create(
            mutableMapOf<String, Any>(
                AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG to kafkaContainer.bootstrapServers
            )
        )

        adminClient.createTopics(
            listOf(
                NewTopic(TemplateHelloActionKafkaConsumer.TOPIC, 1, 1.toShort())
            )
        )
    }
}
