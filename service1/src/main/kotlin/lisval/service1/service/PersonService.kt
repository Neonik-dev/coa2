package lisval.service1.service

import lisval.service1.dto.NewPersonRequest
import lisval.service1.mapper.PersonMapper
import lisval.service1.persistence.model.Person
import lisval.service1.persistence.repository.PersonRepository
import org.springframework.stereotype.Service

@Service
class PersonService(
    val personMapper: PersonMapper,
    val personRepository: PersonRepository,
) {

    fun createPerson(personRequest: NewPersonRequest) {
        val person = personMapper.mapToEntity(personRequest)
        personRepository.save(person)
    }

    fun getAll(): List<Person> {
        val persons = personRepository.findAll()
        return listOf()
    }
}