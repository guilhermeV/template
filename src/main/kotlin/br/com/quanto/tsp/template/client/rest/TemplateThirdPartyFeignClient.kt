package br.com.quanto.tsp.template.client.rest

import br.com.quanto.tsp.template.data.rest.request.TemplateThirdPartyRequest
import br.com.quanto.tsp.template.data.rest.response.TemplateThirdPartyResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping

@FeignClient("thirdparty", url = "\${thirdparty.url}")
interface TemplateThirdPartyFeignClient {

    @GetMapping(
        "/examples/{id}",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun makeGetToThirdPartyServer(@PathVariable("id") id: String): TemplateThirdPartyResponse

    @PostMapping(
        "/examples",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun makePostToThirdPartyServer(templateThirdPartyRequest: TemplateThirdPartyRequest): TemplateThirdPartyResponse
}
