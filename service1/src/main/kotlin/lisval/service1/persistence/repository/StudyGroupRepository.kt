package lisval.service1.persistence.repository

import lisval.service1.persistence.model.Person
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StudyGroupRepository : JpaRepository<Person, String>