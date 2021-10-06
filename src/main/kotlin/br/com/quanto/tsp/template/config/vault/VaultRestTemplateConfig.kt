package br.com.quanto.tsp.template.config.vault

import org.apache.http.client.config.RequestConfig
import org.apache.http.conn.ssl.SSLConnectionSocketFactory
import org.apache.http.conn.ssl.TrustStrategy
import org.apache.http.impl.client.HttpClients
import org.apache.http.impl.client.LaxRedirectStrategy
import org.apache.http.impl.conn.DefaultSchemePortResolver
import org.apache.http.impl.conn.SystemDefaultRoutePlanner
import org.apache.http.ssl.SSLContexts
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.client.ClientHttpRequestFactory
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.vault.config.AbstractVaultConfiguration.ClientFactoryWrapper
import java.net.ProxySelector
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
class VaultRestTemplateConfig {

    @Bean
    @Primary
    @ConditionalOnMissingBean
    fun clientHttpRequestFactoryWrapper(): ClientFactoryWrapper {
        return ClientFactoryWrapper(usingHttpComponents())
    }

    private fun usingHttpComponents(): ClientHttpRequestFactory {
        val sslContext: SSLContext = SSLContexts.custom()
            .loadTrustMaterial(null, TrustStrategy { _: Array<X509Certificate?>?, _: String? -> true })
            .build()

        val httpClientBuilder = HttpClients.custom()
            .setRoutePlanner(SystemDefaultRoutePlanner(DefaultSchemePortResolver.INSTANCE, ProxySelector.getDefault()))
            .setSSLSocketFactory(SSLConnectionSocketFactory(sslContext))
            .setSSLContext(sslContext)
            .setDefaultRequestConfig(
                RequestConfig.custom()
                    .setConnectTimeout(5000)
                    .setSocketTimeout(15000)
                    .setAuthenticationEnabled(true)
                    .build()
            )
            .setRedirectStrategy(LaxRedirectStrategy())

        return HttpComponentsClientHttpRequestFactory(httpClientBuilder.build())
    }
}
