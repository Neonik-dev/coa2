package lisval.service1.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import lisval.service1.persistence.model.enums.Country
import java.time.LocalDate

data class NewPersonRequest (
    @NotBlank
    val name: String,
    val birthday: LocalDate?,
    @NotNull @Min(1)
    val weight: Int,
    val passportId: String?,
    val nationality: Country?,
)