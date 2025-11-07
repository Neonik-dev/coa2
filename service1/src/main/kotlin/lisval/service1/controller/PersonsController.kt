package lisval.service1.controller

import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import lisval.service1.dto.NewPersonRequest
import lisval.service1.dto.PageWrapper
import lisval.service1.dto.PersonResponse
import lisval.service1.persistence.model.enums.Country
import lisval.service1.service.PersonService
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/persons")
@Validated
class PersonsController(
    private val personService: PersonService,
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createPerson(@Valid request: NewPersonRequest) {
        personService.createPerson(request)
    }

    @GetMapping
    fun getAll(
        @RequestParam sort: String?,
        @RequestParam @Min(0) page: Int = 0,
        @RequestParam @Min(0) size: Int = 20,
        @RequestParam passportId: String?,
        @RequestParam birthday: LocalDate?,
        @RequestParam nationality: Country?,
        @RequestParam name: String?,
        @RequestParam @Min(0) weight: Int?,
    ): PageWrapper<PersonResponse> {
        return personService.getAll(sort, page, size, passportId, birthday, nationality, name, weight)
    }
}