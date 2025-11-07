package lisval.service1.mapper

import lisval.service1.dto.NewPersonRequest
import lisval.service1.dto.PageWrapper
import lisval.service1.dto.PersonResponse
import lisval.service1.persistence.model.Person
import org.springframework.stereotype.Component

@Component
class PersonMapper {

    fun mapToEntity(request: NewPersonRequest): Person {
        return Person(
            name = request.name,
            birthday = request.birthday,
            weight = request.weight,
            passportID = request.passportId,
            nationality = request.nationality,
        )
    }

    fun mapToPagePersonResponse(persons: List<Person>, currentPage: Int, totalPage: Int): PageWrapper<PersonResponse> {
        return PageWrapper(
            data = persons.map { mapToPersonResponse(it) },
            currentPage = currentPage,
            totalPage = totalPage,
        )
    }

    fun mapToPersonResponse(person: Person): PersonResponse {
        return PersonResponse(
            name = person.name,
            birthday = person.birthday?.toString(),
            weight = person.weight,
            passportID = person.passportID,
            nationality = person.nationality
        )
    }
}