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
            passportId = request.passportId,
            nationality = request.nationality,
        )
    }

    fun mapToPagePersonResponse(persons: List<Person>, currentPage: Int, totalPages: Int): PageWrapper<PersonResponse> {
        return PageWrapper(
            data = persons.map { mapToPersonResponse(it) },
            currentPage = currentPage,
            totalPages = totalPages,
        )
    }

    fun mapToPersonResponse(person: Person): PersonResponse {
        return PersonResponse(
            id = person.id,
            name = person.name,
            birthday = person.birthday?.toString(),
            weight = person.weight,
            passportId = person.passportId,
            nationality = person.nationality
        )
    }
}