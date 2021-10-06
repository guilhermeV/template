package br.com.quanto.tsp.template.service

import br.com.quanto.tsp.template.model.entity.TemplateExampleObjectEntity
import br.com.quanto.tsp.template.repository.TemplateExampleObjectRepository
import org.springframework.stereotype.Service

@Service
class TemplateExampleObjectFindService(private val templateExampleObjectRepository: TemplateExampleObjectRepository) {

    fun retrieveExampleObjectByMessage(message: String): TemplateExampleObjectEntity? {
        return templateExampleObjectRepository.findByMessage(message)
    }
}
