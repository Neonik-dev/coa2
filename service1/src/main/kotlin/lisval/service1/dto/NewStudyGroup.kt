package lisval.service1.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import lisval.service1.persistence.model.enums.FormOfEducation
import lisval.service1.persistence.model.enums.Semester

data class NewStudyGroup(
    @NotBlank
    val name: String,
    val coordinates: CoordinatesDto,
    @Min(1)
    val studentsCount: Long,
    val formOfEducation: FormOfEducation?,
    val semesterEnum: Semester?,
    val groupAdmin: String?,
)
