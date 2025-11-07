package lisval.service1.mapper

import lisval.service1.dto.NewPersonRequest
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
}