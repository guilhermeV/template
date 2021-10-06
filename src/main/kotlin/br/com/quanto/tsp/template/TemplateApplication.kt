package br.com.quanto.tsp.template

import br.com.quanto.tsp.logpose.config.LogposeConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Import
import org.springframework.scheduling.annotation.EnableScheduling
import java.security.Security

@SpringBootApplication
@Import(
    value = [
        LogposeConfig::class
    ]
)
@EnableFeignClients
@EnableScheduling
class TemplateApplication

fun main(args: Array<String>) {
    Security.setProperty("networkaddress.cache.ttl", "60")
    runApplication<TemplateApplication>(*args)
}
