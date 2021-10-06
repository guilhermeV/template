package br.com.quanto.tsp.template.testutils

import org.testcontainers.containers.PostgreSQLContainer

class QuantoPostgreSQLContainer(imageName: String) : PostgreSQLContainer<QuantoPostgreSQLContainer>(imageName)
