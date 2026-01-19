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

@XmlRootElement(name = "StudyGroup")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "StudyGroupResponse",
    propOrder = ["id", "name", "coordinates", "creationDate", "studentsCount",
        "formOfEducation", "semesterEnum", "groupAdmin"]
)
data class StudyGroupResponse(
    @field:XmlElement(name = "id", required = true)
    @field:XmlID
    @field:XmlSchemaType(name = "positiveInteger")
    val id: Long,

    @field:XmlElement(name = "name", required = true)
    val name: String,

    @field:XmlElement(name = "coordinates", required = true)
    val coordinates: CoordinatesDto,

    @field:XmlElement(name = "creationDate", required = true)
    @field:XmlSchemaType(name = "date")
    @field:XmlJavaTypeAdapter(LocalDateAdapter::class)
    val creationDate: LocalDate,

    @field:XmlElement(name = "studentsCount")
    @field:XmlSchemaType(name = "positiveInteger")
    val studentsCount: Long?,

    @field:XmlElement(name = "formOfEducation")
    @field:XmlJavaTypeAdapter(FormOfEducationAdapter::class)
    val formOfEducation: FormOfEducation?,

    @field:XmlElement(name = "semester")
    @field:XmlJavaTypeAdapter(SemesterAdapter::class)
    val semesterEnum: Semester?,

    @field:XmlElement(name = "groupAdmin")
    val groupAdmin: PersonResponse?,
) {
    constructor() : this(0, "", CoordinatesDto(), LocalDate.now(), null, null, null, null)

    constructor(
        id: Long,
        name: String,
        coordinates: CoordinatesDto,
        creationDate: String,
        studentsCount: Long?,
        formOfEducation: FormOfEducation?,
        semesterEnum: Semester?,
        groupAdmin: PersonResponse?
    ) : this(
        id = id,
        name = name,
        coordinates = coordinates,
        creationDate = LocalDate.parse(creationDate),
        studentsCount = studentsCount,
        formOfEducation = formOfEducation,
        semesterEnum = semesterEnum,
        groupAdmin = groupAdmin
    )
}