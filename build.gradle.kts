import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

plugins {
    java
    idea
    id("org.springframework.boot") version "2.5.4"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.5.30"
    kotlin("plugin.spring") version "1.5.30"
    id("org.jlleitschuh.gradle.ktlint") version "10.1.0"
    id("org.jlleitschuh.gradle.ktlint-idea") version "10.1.0"
    id("org.liquibase.gradle") version "2.0.4"
    jacoco
    id("org.sonarqube") version "3.3"
}

group = "br.com.quanto.tsp.template"
version = "0.0.3"

java.sourceCompatibility = JavaVersion.VERSION_16

val ghPkgUsername by extra(System.getenv("GH_PACKAGES_USERNAME") ?: project.property("github.username").toString())
val ghPkgPassword by extra(System.getenv("GH_PACKAGES_PASSWORD") ?: project.property("github.token").toString())
val sonarquebeKey by extra(System.getenv("SONAR_KEY") ?: "tsp-kotlin-ms-template")
val protobucketVersion = "1.20.6"
val tagVersion by extra(System.getenv("BUILD_TAG_VERSION") ?: version)
val exposedVersion = "0.34.1"
val liquibaseVersion = "4.4.3"
val testContainerVersion = "1.16.0"

repositories {
    mavenCentral()
    maven {
        url = uri("https://plugins.gradle.org/m2/")
    }
    maven {
        url = uri("https://maven.pkg.github.com/contaquanto/tspprotobucket")
        credentials {
            username = ghPkgUsername
            password = ghPkgPassword
        }
    }
    maven {
        url = uri("https://maven.pkg.github.com/contaquanto/tsp-logpose")
        credentials {
            username = ghPkgUsername
            password = ghPkgPassword
        }
    }
}

springBoot {
    buildInfo {
        properties {
            additional = mapOf(
                "tagVersion" to tagVersion,
            )
        }
    }
}

extra["springCloudVersion"] = "2020.0.3"

dependencies {
    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // Spring REST
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    // REST Client
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")

    // Kafka
    implementation("org.springframework.kafka:spring-kafka")

    // Logs in JSON Format
    implementation("org.springframework.boot:spring-boot-starter-log4j2")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.lmax:disruptor:3.4.4")
    // YAML log4 config
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml")
    // Idiomatic Logging in Kotlin
    implementation("io.github.microutils:kotlin-logging:2.0.11")

    // Tracing - Open Tracing
    implementation("org.springframework.cloud:spring-cloud-starter-sleuth")
    implementation("org.springframework.cloud:spring-cloud-sleuth-zipkin")
    implementation("io.opentracing:opentracing-api:0.33.0")
    // Grpc - Tracing
    implementation("io.zipkin.brave:brave-instrumentation-grpc:5.13.3")

    // Health and Metrics
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")

    // GRPC
    // Se você for só usar o GRPC cliente use :
    // implementation("net.devh:grpc-client-spring-boot-starter:2.12.0.RELEASE")
    // Se você for usar tanto cliente quanto server grpc use:
    implementation("net.devh:grpc-spring-boot-starter:2.12.0.RELEASE")

    // GRPC Kafka Serializer/Deserializers
    implementation("com.github.daniel-shuy:kafka-protobuf-serde:2.2.0")

    // GRPC ProtoBuf from ProtoBucket import
    implementation("br.com.quanto.tsp.protobucket:event:$protobucketVersion")
    implementation("br.com.quanto.tsp.protobucket:template:$protobucketVersion")
    implementation("br.com.quanto.tsp.protobucket:templatethirdparty:$protobucketVersion")
    implementation("br.com.quanto.tsp.protobucket:action:$protobucketVersion")

    // AuditLogLibrary - LOGPOSE
    implementation("br.com.quanto.tsp:logpose:2.2.1")

    // Vault
    implementation("org.springframework.cloud:spring-cloud-starter-vault-config")
    implementation("org.springframework.cloud:spring-cloud-starter-bootstrap")
    // SQL
    implementation("org.postgresql:postgresql:42.2.23")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposedVersion")
    implementation("org.jetbrains.exposed:spring-transaction:$exposedVersion")
    // SQL VAULT
    implementation("org.springframework.cloud:spring-cloud-vault-config-databases")

    // LIQUIBASE
    implementation("org.liquibase:liquibase-core:$liquibaseVersion")
    liquibaseRuntime("org.liquibase:liquibase-core:$liquibaseVersion")
    liquibaseRuntime("org.liquibase:liquibase-groovy-dsl:3.0.2")
    liquibaseRuntime("org.postgresql:postgresql:42.2.23")
    liquibaseRuntime("org.yaml:snakeyaml:1.29")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude("org.junit.vintage", "junit-vintage-engine")
        exclude("org.mockito", "mockito-core")
        exclude("org.mockito", "mockito-junit-jupiter")
    }
    // Cuidado ao aumentar a versão dessa dependencia, pois pode gerar conflito com o tspprotobucket
    // Fortemente recomendado manter a mesma versão do io.grpc do build.gradle do repositorio tspprotobucket
    testImplementation("io.grpc:grpc-testing")
    // INTEGRATION TESTS TESTCONTAINERS
    testImplementation("org.testcontainers:testcontainers:$testContainerVersion")
    testImplementation("org.testcontainers:junit-jupiter:$testContainerVersion")
    testImplementation("org.testcontainers:kafka:$testContainerVersion")
    // SQL
    testImplementation("org.testcontainers:postgresql:$testContainerVersion")
    // VAULT
    testImplementation("org.testcontainers:vault:$testContainerVersion")

    // REST INTEGRATION TESTS
    testImplementation("org.springframework.cloud:spring-cloud-starter-contract-stub-runner")
    // GRPC INTEGRATION TESTS
    testImplementation("org.grpcmock:grpcmock-spring-boot:0.6.0")
    // FAKER
    testImplementation("io.github.serpro69:kotlin-faker:1.7.1")
    // KOTEST
    testImplementation("io.kotest:kotest-assertions-core-jvm:4.6.2")
    // MOCKK
    testImplementation("io.mockk:mockk:1.12.0")
    testImplementation("com.ninja-squad:springmockk:3.0.1")
}

configurations.all {
    exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
    resolutionStrategy.eachDependency {
        if (requested.group == "io.grpc") {
            useVersion("1.37.0")
            because("Guarantee of use the same version that are in use in tspprotobucket")
        }
    }
}

dependencyManagement.imports {
    mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict", "-Xemit-jvm-type-annotations")
        jvmTarget = "16"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    finalizedBy("jacocoTestReport")
    doLast {
        println("View code coverage at:")
        println("file://$buildDir/reports/jacoco/test/html/index.html")
    }
}

apply(plugin = "org.jlleitschuh.gradle.ktlint")
apply(plugin = "org.jlleitschuh.gradle.ktlint-idea")

tasks.getByName<JacocoReport>("jacocoTestReport") {
    classDirectories.setFrom(
        files(
            classDirectories.files.map {
                fileTree(it) {
                    exclude(
                        "**/TemplateApplication.class",
                        "**/TemplateApplicationKt.class",
                        "**/Database.class",
                        "**/DatabaseKt.class",
                        "**/DatabaseConfig.class",
                        "**/DatabaseConfigKt.class",
                        "**/KafkaConsumerConfig.class",
                        "**/KafkaConsumerConfigKt.class"
                    )
                }
            }
        )
    )
    reports {
        html.outputLocation.set(File("$buildDir/jacocoHtml"))
        html.required.set(true)
        xml.required.set(true)
        csv.required.set(true)
    }
}

tasks.withType<BootBuildImage> {
    builder = "paketobuildpacks/builder:base"
}

jacoco {
    toolVersion = "0.8.7"
}

sonarqube.properties {
    property("sonar.projectKey", sonarquebeKey)
    property("sonar.organization", "contaquanto")
    property("sonar.host.url", "https://sonarcloud.io")
}
