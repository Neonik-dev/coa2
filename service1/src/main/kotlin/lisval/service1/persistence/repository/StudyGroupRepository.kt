package lisval.service1.persistence.repository

import lisval.service1.persistence.model.StudyGroup
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StudyGroupRepository : JpaRepository<StudyGroup, Long> {
    fun findFirstByOrderByCreationDateAsc(): StudyGroup?
}