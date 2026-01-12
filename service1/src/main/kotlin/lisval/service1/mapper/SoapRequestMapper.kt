package lisval.service1.mapper

import lisval.service1.dto.CoordinatesDto
import lisval.service1.dto.NewStudyGroup
import lisval.service1.dto.v2.CreateGroupRequest
import lisval.service1.dto.v2.PersonType
import lisval.service1.persistence.model.Person
import lisval.service1.persistence.model.enums.Country
import lisval.service1.persistence.model.enums.FormOfEducation
import lisval.service1.persistence.model.enums.Semester
import org.springframework.stereotype.Component

@Component
class SoapRequestMapper {

    fun mapCreateGroupRequestToDto(request: CreateGroupRequest): NewStudyGroup {
        return NewStudyGroup(
            name = request.name!!,
            coordinates = CoordinatesDto(
                x = request.x?.toInt()!!,
                y = request.y?.toInt()!!
            ),
            studentsCount = request.studentsCount!!,
            formOfEducation = FormOfEducation.valueOf(request.formOfEducation!!),
            semesterEnum = Semester.valueOf(request.semesterEnum!!),
            groupAdmin = request.groupAdmin?.let { mapPersonTypeToPerson(it) } as String,
        )
    }

    private fun mapPersonTypeToPerson(personType: PersonType): Person {
        return Person(
            name = personType.name!!,
            birthday = personType.birthday!!,
            weight = personType.weight!!,
            passportId = personType.passportId,
            id = java.util.UUID.randomUUID().toString(),
            nationality = Country.CHINA
        )
    }
}