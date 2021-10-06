package br.com.quanto.tsp.template.controller.thirdparty

import br.com.quanto.tsp.template.data.rest.request.TemplateThirdPartyRequest
import br.com.quanto.tsp.template.data.rest.response.TemplateThirdPartyResponse
import mu.KotlinLogging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/examples")
class TemplateThirdPartyRestController {

    private companion object {
        val log = KotlinLogging.logger {}
    }

    @GetMapping("/{id}")
    fun getThirdPartyExampleMock(@PathVariable("id") id: String): TemplateThirdPartyResponse {
        log.info { "third party get example request, id=$id" }
        return TemplateThirdPartyResponse(id, UUID.randomUUID().toString())
    }

    @PostMapping
    fun postThirdPartyExampleMock(@RequestBody body: TemplateThirdPartyRequest): TemplateThirdPartyResponse {
        log.info { "post third party get example request, id=$body" }
        return TemplateThirdPartyResponse(body.message, UUID.randomUUID().toString())
    }
}
