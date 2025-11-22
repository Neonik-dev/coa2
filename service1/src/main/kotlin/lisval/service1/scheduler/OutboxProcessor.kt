package lisval.service1.scheduler

import co.elastic.clients.elasticsearch.ElasticsearchClient
import co.elastic.clients.elasticsearch._types.ElasticsearchException
import com.fasterxml.jackson.databind.ObjectMapper
import lisval.service1.persistence.model.OutboxEvent
import lisval.service1.persistence.model.enums.OutboxEventType
import lisval.service1.persistence.repository.OutboxEventRepository
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class OutboxProcessor(
    private val outboxEventRepository: OutboxEventRepository,
    private val esClient: ElasticsearchClient,
    private val mapper: ObjectMapper,
) {
    @Scheduled(fixedDelay = 1000)
    fun process() {
        val events= outboxEventRepository.findTop50ByProcessedFalse()
        for (event in events) {
            try {
                if (event.eventType == OutboxEventType.DELETE){
                    deleteFromElastic(event)
                } else {
                    indexToElastic(event)
                }
                event.processed = true
                outboxEventRepository.save(event)
                log.info("Processed event: $event")
            } catch (ex: ElasticsearchException) {
                log.error("Error processing event: $event. Response: ${ex.response()}", ex)
            }
        }
    }

    private fun indexToElastic(event: OutboxEvent) {
        val payloadNode = mapper.readTree(event.payload)

        val doc = mapOf(
            "aggregateId" to event.aggregateId,
            "aggregateType" to event.aggregateType,
            "payload" to payloadNode,
        )

        esClient.index { i ->
            i.index(event.aggregateType)
                .id(event.aggregateId)
                .document(doc)
        }
    }

    private fun deleteFromElastic(event: OutboxEvent) {
        esClient.delete { d ->
            d.index(event.aggregateType)
                .id(event.aggregateId)
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(OutboxProcessor::class.java)
    }
}