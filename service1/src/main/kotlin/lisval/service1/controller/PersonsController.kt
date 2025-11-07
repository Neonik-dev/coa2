package lisval.service1.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/persons")
class PersonsController(

) {

    @PostMapping
    fun addPerson() {

    }

    @GetMapping
    fun getAll() {

    }
}