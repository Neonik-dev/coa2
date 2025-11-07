package lisval.service1.service

import jakarta.persistence.EntityManager
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.Order
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import lisval.service1.dto.NewPersonRequest
import lisval.service1.dto.PageWrapper
import lisval.service1.dto.PersonResponse
import lisval.service1.mapper.PersonMapper
import lisval.service1.persistence.model.Person
import lisval.service1.persistence.model.enums.Country
import lisval.service1.persistence.repository.PersonRepository
import org.springframework.stereotype.Service
import java.time.LocalDate
import kotlin.math.ceil

@Service
class PersonService(
    private val personMapper: PersonMapper,
    private val personRepository: PersonRepository,
    private val entityManager: EntityManager,
) {

    fun createPerson(personRequest: NewPersonRequest) {
        val person = personMapper.mapToEntity(personRequest)
        personRepository.save(person)
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
        val sortPredicates = generateSortPredicates(builder, root, sort)
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
        return listOf(
            generatePredicate(builder, root, passportId, "passport_id"),
            generatePredicate(builder, root, birthday, "birthday"),
            generatePredicate(builder, root, nationality?.name, "nationality"),
            generatePredicate(builder, root, name, "name"),
            generatePredicate(builder, root, weight, "weight"),
        ).requireNoNulls().toTypedArray()
    }

    private fun <T> generatePredicate(
        builder: CriteriaBuilder,
        root: Root<Person>,
        value: T?,
        fieldName: String
    ): Predicate? {
        return value?.let {
            builder.equal(
                root.get<T>(fieldName),
                value
            )
        }
    }

    private fun generateSortPredicates(
        builder: CriteriaBuilder,
        root: Root<Person>,
        sort: String?
    ): List<Order> {
        sort ?: return emptyList()

        return sort
            .trim()
            .split(",")
            .mapNotNull {
                val e = it.split(":")
                val name = root.get<Any>(e[0])
                when {
                    e.size == 1 -> {
                        builder.asc(name)
                    }

                    e.size == 2 && e[1] == "desc" -> {
                        builder.desc(name)
                    }

                    else -> null
                }
            }
    }
}