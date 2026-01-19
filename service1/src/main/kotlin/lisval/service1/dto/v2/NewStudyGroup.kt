package lisval.service1.dto.v2

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlRootElement
import jakarta.xml.bind.annotation.XmlType
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter

@XmlRootElement(name = "StudyGroup")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = ["name", "coordinates", "studentsCount", "formOfEducation", "semesterEnum", "groupAdmin"])
data class NewStudyGroup(
    @field:XmlElement(name = "name", required = true)
    @field:NotBlank(message = "Name cannot be blank")
    val name: String,

    @field:XmlElement(name = "coordinates", required = true)
    val coordinates: CoordinatesDto,

    @field:XmlElement(name = "studentsCount", required = true)
    @field:Min(value = 1, message = "Students count must be at least 1")
    val studentsCount: Long,

    @field:XmlElement(name = "formOfEducation")
    @field:XmlJavaTypeAdapter(FormOfEducationAdapter::class)  // Адаптер для enum
    val formOfEducation: FormOfEducation? = null,

    @field:XmlElement(name = "semester")
    @field:XmlJavaTypeAdapter(SemesterAdapter::class)  // Адаптер для enum
    val semesterEnum: Semester? = null,

    @field:XmlElement(name = "groupAdmin")
    val groupAdmin: String? = null,
) {
    constructor() : this("", CoordinatesDto(), 1, null, null, null)
}