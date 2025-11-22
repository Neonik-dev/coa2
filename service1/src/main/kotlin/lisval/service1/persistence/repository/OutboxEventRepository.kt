package lisval.service1.persistence.repository

import lisval.service1.persistence.model.OutboxEvent
import org.springframework.data.jpa.repository.JpaRepository


interface OutboxEventRepository : JpaRepository<OutboxEvent, Long> {
    fun findTop50ByProcessedFalse(): List<OutboxEvent>
}
