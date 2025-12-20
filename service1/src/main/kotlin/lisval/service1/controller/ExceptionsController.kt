package lisval.service1.controller

import lisval.service1.dto.ErrorDto
import lisval.service1.exceptions.EntityByFilterNotFound
import lisval.service1.exceptions.GroupNotFound
import lisval.service1.exceptions.PersonNotFound
import lisval.service1.exceptions.ValidationException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionsController {

    @ExceptionHandler(PersonNotFound::class)
    fun handleInvalidOrderStatusException(e: PersonNotFound): ResponseEntity<Unit> {
        logger.error("Something happened..")
        logger.error(e.stackTraceToString())
        return ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(GroupNotFound::class)
    fun handleInvalidOrderStatusException(e: GroupNotFound): ResponseEntity<Unit> {
        logger.error("Something happened..")
        logger.error(e.stackTraceToString())
        return ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(EntityByFilterNotFound::class)
    fun handleInvalidOrderStatusException(e: EntityByFilterNotFound): ResponseEntity<Unit> {
        logger.error("Something happened..")
        logger.error(e.stackTraceToString())
        return ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(ValidationException::class)
    fun handleInvalidOrderStatusException(e: ValidationException): ResponseEntity<ErrorDto> {
        logger.error("Something happened.." + e.message)
        logger.error(e.stackTraceToString())
        return ResponseEntity(ErrorDto(e.message.toString()), HttpStatus.BAD_REQUEST)
    }

    private companion object {
        val logger: Logger = LoggerFactory.getLogger(ExceptionsController::class.java)
    }
}