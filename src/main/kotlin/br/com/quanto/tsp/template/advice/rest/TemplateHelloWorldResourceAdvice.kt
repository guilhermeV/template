package br.com.quanto.tsp.template.advice.rest

import br.com.quanto.tsp.template.exception.rest.TemplateUnsuccessCommunicationWithThirdPartyException
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class TemplateHelloWorldResourceAdvice {

    private companion object {
        val log = KotlinLogging.logger {}
    }

    @ExceptionHandler(value = [(TemplateUnsuccessCommunicationWithThirdPartyException::class)])
    fun handleFailure(templateUnsuccessCommunicationWithThirdPartyException: TemplateUnsuccessCommunicationWithThirdPartyException): ResponseEntity<String> {
        log.error { "Error communication with third party service, message=${templateUnsuccessCommunicationWithThirdPartyException.message}" }
        return ResponseEntity("{\"status\": 500}", HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
