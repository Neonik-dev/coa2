package lisval.service1.dto

import lisval.service1.persistence.model.enums.FormOfEducation
import lisval.service1.persistence.model.enums.Semester
import java.time.LocalDate

data class StudyGroupResponse(
    val id: Long,
    val name: String,
    val coordinates: CoordinatesDto,
    val creationDate: LocalDate,
    val studentsCount: Long?,
    val formOfEducation: FormOfEducation?,
    val semesterEnum: Semester?,
    val groupAdmin: PersonResponse?,
)
