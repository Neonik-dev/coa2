package lisval.service1.persistence.repository

import lisval.service1.persistence.model.GroupByFormOfEducation
import lisval.service1.persistence.model.StudyGroup
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface StudyGroupRepository : JpaRepository<StudyGroup, Long> {
    fun findFirstByOrderByCreationDateAsc(): StudyGroup?

    @Query("SELECT new lisval.service1.persistence.model.GroupByFormOfEducation(sg.formOfEducation, COUNT(sg.id)) FROM StudyGroup sg GROUP BY sg.formOfEducation")
    fun findGroupByFormOfEducation(): List<GroupByFormOfEducation?>
}