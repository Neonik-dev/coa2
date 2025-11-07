package lisval.service1.service

import lisval.service1.persistence.model.Person
import lisval.service1.persistence.repository.PersonRepository
import org.springframework.stereotype.Service

@Service
class PersonService(
    val personRepository: PersonRepository,
) {

    fun getAll(): List<Person> {
        val persons = personRepository.findAll()
        return listOf()
    }
}