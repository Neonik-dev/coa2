package lisval.service1.dto.v2

import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlID
import jakarta.xml.bind.annotation.XmlRootElement
import jakarta.xml.bind.annotation.XmlSchemaType
import jakarta.xml.bind.annotation.XmlType
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter
import java.time.LocalDate

@XmlRootElement(name = "Person")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "PersonResponse",
    propOrder = ["id", "name", "birthday", "weight", "passportId", "nationality"]
)
data class PersonResponse(
    @field:XmlElement(name = "id", required = true)
    @field:XmlID  // Уникальный идентификатор для XML
    val id: String,

    @field:XmlElement(name = "name", required = true)
    val name: String,

    @field:XmlElement(name = "birthday")
    @field:XmlSchemaType(name = "date")  // Указываем тип XML Schema
    @field:XmlJavaTypeAdapter(LocalDateAdapter::class)
    val birthday: String?,  // В SOAP обычно используется String для дат

    @field:XmlElement(name = "weight", required = true)
    @field:XmlSchemaType(name = "positiveInteger")  // Только положительные числа
    val weight: Int,

    @field:XmlElement(name = "passportId")
    val passportId: String?,

    @field:XmlElement(name = "nationality")
    @field:XmlJavaTypeAdapter(CountryAdapter::class)
    val nationality: Country?,
) {

    constructor() : this("", "", "", 0, null, null)

    // Альтернативный конструктор с LocalDate
    constructor(
        id: String,
        name: String,
        birthday: LocalDate?,
        weight: Int,
        passportId: String?,
        nationality: Country?
    ) : this(
        id = id,
        name = name,
        birthday = birthday?.toString(),
        weight = weight,
        passportId = passportId,
        nationality = nationality
    )
}