package lisval.service1.persistence.model

import lisval.service1.persistence.model.enums.FormOfEducation

data class GroupByFormOfEducation(
    val formOfEducation: FormOfEducation,
    val count: Long,
)
