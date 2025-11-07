package lisval.service1.controller

import lisval.service1.dto.NewPersonRequest
import lisval.service1.service.PersonService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/persons")
class PersonsController(
    private val personService: PersonService,
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createPerson(request: NewPersonRequest) {
        personService.createPerson(request)
    }

    @GetMapping
    fun getAll() {

    }
}