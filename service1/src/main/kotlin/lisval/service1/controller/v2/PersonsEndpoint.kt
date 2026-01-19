package lisval.service1.controller.v2

import lisval.service1.dto.NewPersonRequest
import lisval.service1.dto.v2.CreatePersonRequest
import lisval.service1.dto.v2.CreatePersonResponse
import lisval.service1.persistence.model.enums.Country
import lisval.service1.service.PersonService
import org.springframework.ws.server.endpoint.annotation.Endpoint
import org.springframework.ws.server.endpoint.annotation.PayloadRoot
import org.springframework.ws.server.endpoint.annotation.RequestPayload
import org.springframework.ws.server.endpoint.annotation.ResponsePayload
import org.springframework.ws.soap.server.endpoint.annotation.SoapAction

@Endpoint
class PersonsEndpoint(
    private val personService: PersonService
) {

    companion object {
        private const val NAMESPACE_URI = "https://localhost/api/persons"
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "createPersonRequest")
    @ResponsePayload
    @SoapAction("https://localhost/api/persons/createPerson")
    fun createPerson(@RequestPayload request: CreatePersonRequest): CreatePersonResponse {

        val newPersonRequest = NewPersonRequest(
            name = request.name!!,
            birthday = request.birthday,
            weight = request.weight!!,
            passportId = request.passportId,
            nationality = request.nationality?.let { Country.valueOf(it) }
        )

        var personId = personService.createPerson(newPersonRequest)

        return CreatePersonResponse().apply {
            status = "SUCCESS"
            message = "Person created successfully"
            personId = personId
        }
    }
}