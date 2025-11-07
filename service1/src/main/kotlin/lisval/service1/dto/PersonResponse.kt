package lisval.service1.dto

import lisval.service1.persistence.model.enums.Country

data class PersonResponse(
    val name: String,
    val birthday: String?,
    val weight: Int,
    val passportID: String?,
    val nationality: Country?,
)