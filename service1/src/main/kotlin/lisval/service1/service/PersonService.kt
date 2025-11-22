package lisval.service1.service

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.persistence.EntityManager
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import jakarta.transaction.Transactional
import lisval.service1.dto.NewPersonRequest
import lisval.service1.dto.PageWrapper
import lisval.service1.dto.PersonResponse
import lisval.service1.mapper.PersonMapper
import lisval.service1.persistence.model.OutboxEvent
import lisval.service1.persistence.model.Person
import lisval.service1.persistence.model.enums.Country
import lisval.service1.persistence.model.enums.OutboxEventType
import lisval.service1.persistence.repository.OutboxEventRepository
import lisval.service1.persistence.repository.PersonRepository
import lisval.service1.utils.CriteriaApiUtils
import org.springframework.stereotype.Service
import java.time.LocalDate
import kotlin.math.ceil


@Service
class PersonService(
    private val personMapper: PersonMapper,
    private val personRepository: PersonRepository,
    private val entityManager: EntityManager,
    private val outboxEventRepository: OutboxEventRepository,
    private val mapper: ObjectMapper
) {

    @Transactional
    fun createPerson(personRequest: NewPersonRequest) {
        val person = personMapper.mapToEntity(personRequest)
        val saved = personRepository.save(person)
        val event = try {
            OutboxEvent(
                aggregateType = "person",
                aggregateId = saved.id,
                eventType = OutboxEventType.CREATE,
                payload = mapper.writeValueAsString(saved),
            )
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

        outboxEventRepository.save(event)
    }

    fun getAll(
        sort: String?,
        page: Int,
        size: Int,
        passportId: String?,
        birthday: LocalDate?,
        nationality: Country?,
        name: String?,
        weight: Int?,
    ): PageWrapper<PersonResponse> {
        val builder = entityManager.criteriaBuilder

        val countQuery = builder.createQuery(Long::class.java)
        var root = countQuery.from(Person::class.java)
        var predicates = generatePredicates(builder, root, passportId, birthday, nationality, name, weight)
        countQuery.select(builder.count(root)).where(*predicates)
        val countRaw = entityManager.createQuery(countQuery).singleResult
        val countPage = ceil(countRaw / size.toDouble()).toInt()

        val criteriaQuery = builder.createQuery(Person::class.java)
        root = criteriaQuery.from(Person::class.java)
        predicates = generatePredicates(builder, root, passportId, birthday, nationality, name, weight)
        val sortPredicates = CriteriaApiUtils.generateSortPredicates(builder, root, sort)
        val select = criteriaQuery.select(root).where(*predicates).orderBy(sortPredicates)
        val persons = entityManager.createQuery(select).setFirstResult((page) * size).setMaxResults(size).resultList
        return personMapper.mapToPagePersonResponse(persons, page, countPage)
    }

    private fun generatePredicates(
        builder: CriteriaBuilder,
        root: Root<Person>,
        passportId: String?,
        birthday: LocalDate?,
        nationality: Country?,
        name: String?,
        weight: Int?,
    ): Array<Predicate> {
        return listOfNotNull(
            CriteriaApiUtils.generatePredicate(builder, root, passportId, "passport_id"),
            CriteriaApiUtils.generatePredicate(builder, root, birthday, "birthday"),
            CriteriaApiUtils.generatePredicate(builder, root, nationality?.name, "nationality"),
            CriteriaApiUtils.generatePredicate(builder, root, name, "name"),
            CriteriaApiUtils.generatePredicate(builder, root, weight, "weight"),
        ).toTypedArray()
    }
}