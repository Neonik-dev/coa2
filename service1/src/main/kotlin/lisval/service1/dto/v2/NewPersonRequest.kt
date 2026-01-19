package lisval.service1.dto.v2

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlRootElement
import jakarta.xml.bind.annotation.XmlSchemaType
import jakarta.xml.bind.annotation.XmlType
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter
import java.time.LocalDate

@XmlRootElement(name = "CreatePersonRequest")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "NewPersonRequest",
    propOrder = ["name", "birthday", "weight", "passportId", "nationality"]
)
data class NewPersonRequest(
    @field:XmlElement(name = "name", required = true)
    @field:NotBlank(message = "Name cannot be blank")
    @field:Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters")
    val name: String,

    @field:XmlElement(name = "birthday")
    @field:XmlSchemaType(name = "date")
    @field:XmlJavaTypeAdapter(LocalDateAdapter::class)
    val birthday: LocalDate?,

    @field:XmlElement(name = "weight", required = true)
    @field:Min(value = 1, message = "Weight must be at least 1")
    @field:Max(value = 300, message = "Weight must not exceed 300")
    @field:XmlSchemaType(name = "positiveInteger")
    val weight: Int,

    @field:XmlElement(name = "passportId")
    @field:Pattern(regexp = "[A-Z]{2}\\d{7}", message = "Passport ID must be in format: XX1234567")
    @field:Size(min = 9, max = 9, message = "Passport ID must be 9 characters")
    val passportId: String?,

    @field:XmlElement(name = "nationality")
    @field:XmlJavaTypeAdapter(CountryAdapter::class)
    val nationality: Country?,
) {
    // Обязательный конструктор без параметров для JAXB
    constructor() : this("", null, 0, null, null)
}