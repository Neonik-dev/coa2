package lisval.service1.service

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.persistence.EntityManager
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import lisval.service1.dto.NewStudyGroup
import lisval.service1.dto.PageWrapper
import lisval.service1.dto.StudyGroupResponse
import lisval.service1.exceptions.EntityByFilterNotFound
import lisval.service1.exceptions.GroupNotFound
import lisval.service1.exceptions.PersonNotFound
import lisval.service1.mapper.StudyGroupMapper
import lisval.service1.persistence.model.GroupByFormOfEducation
import lisval.service1.persistence.model.OutboxEvent
import lisval.service1.persistence.model.StudyGroup
import lisval.service1.persistence.model.enums.FormOfEducation
import lisval.service1.persistence.model.enums.OutboxEventType
import lisval.service1.persistence.model.enums.Semester
import lisval.service1.persistence.repository.OutboxEventRepository
import lisval.service1.persistence.repository.PersonRepository
import lisval.service1.persistence.repository.StudyGroupRepository
import lisval.service1.utils.CriteriaApiUtils
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import kotlin.math.ceil

@Service
class StudyGroupService(
    private val studyGroupRepository: StudyGroupRepository,
    private val personRepository: PersonRepository,
    private val studentGroupMapper: StudyGroupMapper,
    private val entityManager: EntityManager,
    private val mapper: ObjectMapper,
    private val outboxEventRepository: OutboxEventRepository,
    ) {
    @Transactional
    fun createGroup(request: NewStudyGroup) {
        val admin = request.groupAdmin?.let {
            personRepository.findByIdOrNull(it) ?: throw PersonNotFound(it)
        }
        val studyGroup = studentGroupMapper.mapToEntity(request, admin)
        val saved = studyGroupRepository.save(studyGroup)
        val event = try {
            OutboxEvent(
                aggregateType = "study-group",
                aggregateId = saved.id.toString(),
                eventType = OutboxEventType.CREATE,
                payload = mapper.writeValueAsString(saved),
            )
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

        outboxEventRepository.save(event)
    }

    fun getById(id: Long): StudyGroup {
        return studyGroupRepository.findByIdOrNull(id) ?: throw GroupNotFound(id)
    }

    @Transactional
    fun putById(id: Long, request: NewStudyGroup) {
        val studyGroup = studyGroupRepository.findByIdOrNull(id) ?: throw GroupNotFound(id)
        val admin = when (request.groupAdmin) {
            null -> null
            studyGroup.groupAdmin?.id -> studyGroup.groupAdmin
            else -> personRepository.findByIdOrNull(request.groupAdmin) ?: throw PersonNotFound(request.groupAdmin)
        }
        studentGroupMapper.enrichToStudyGroup(studyGroup, request, admin)
        val saved = studyGroupRepository.save(studyGroup)
        val event = try {
            OutboxEvent(
                aggregateType = "study-group",
                aggregateId = saved.id.toString(),
                eventType = OutboxEventType.UPDATE,
                payload = mapper.writeValueAsString(saved),
            )
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

        outboxEventRepository.save(event)
    }

    @Transactional
    fun removeById(id: Long) {
        val event = try {
            OutboxEvent(
                aggregateType = "study-group",
                aggregateId = id.toString(),
                eventType = OutboxEventType.DELETE,
                payload = null
            )
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

        outboxEventRepository.save(event)
        return studyGroupRepository.deleteById(id)
    }

    fun getByMinCreationDate() : StudyGroup {
        return studyGroupRepository.findFirstByOrderByCreationDateAsc() ?: throw EntityByFilterNotFound()
    }

    fun getGroupByFormOfEducation() : List<GroupByFormOfEducation> {
        return studyGroupRepository.findGroupByFormOfEducation().filterNotNull()
    }

    fun getLtFormOfEducation(formOfEducation: FormOfEducation) : List<StudyGroup> {
        return studyGroupRepository.findByFormOfEducationLessThan(formOfEducation)
    }

    fun getAll(
        sort: String?,
        page: Int,
        size: Int,
        id: Long?,
        name: String?,
        x: String?,
        y: String?,
        creationDate: LocalDate?,
        studentsCount: Long?,
        ltStudentsCount: Long?,
        gtStudentsCount: Long?,
        formOfEducation: FormOfEducation?,
        semesterEnum: Semester?,
        groupAdmin: String?,
    ): PageWrapper<StudyGroupResponse> {
        val builder = entityManager.criteriaBuilder

        val countQuery = builder.createQuery(Long::class.java)
        var root = countQuery.from(StudyGroup::class.java)
        var predicates = generatePredicates(builder, root, id, name, x, y, creationDate, studentsCount, ltStudentsCount, gtStudentsCount, formOfEducation, semesterEnum, groupAdmin)
        countQuery.select(builder.count(root)).where(*predicates)
        val countRaw = entityManager.createQuery(countQuery).singleResult
        val countPage = ceil(countRaw / size.toDouble()).toInt()

        val criteriaQuery = builder.createQuery(StudyGroup::class.java)
        root = criteriaQuery.from(StudyGroup::class.java)
        predicates = generatePredicates(builder, root, id, name, x, y, creationDate, studentsCount, ltStudentsCount, gtStudentsCount, formOfEducation, semesterEnum, groupAdmin)
        val sortPredicates = CriteriaApiUtils.generateSortPredicates(builder, root, sort)
        val select = criteriaQuery.select(root).where(*predicates).orderBy(sortPredicates)
        val persons = entityManager.createQuery(select).setFirstResult((page) * size).setMaxResults(size).resultList
        return studentGroupMapper.mapToPageStudyGroupResponse(persons, page, countPage)
    }

    private fun generatePredicates(
        builder: CriteriaBuilder,
        root: Root<StudyGroup>,
        id: Long?,
        name: String?,
        x: String?,
        y: String?,
        creationDate: LocalDate?,
        studentsCount: Long?,
        ltStudentsCount: Long?,
        gtStudentsCount: Long?,
        formOfEducation: FormOfEducation?,
        semesterEnum: Semester?,
        groupAdmin: String?,
    ): Array<Predicate> {
        return listOfNotNull(
            CriteriaApiUtils.generatePredicate(builder, root, id, "id"),
            name?.let { builder.like(root.get("name"), "%$it%") },
            CriteriaApiUtils.generatePredicate(builder, root, x, "coordinate_x"),
            CriteriaApiUtils.generatePredicate(builder, root, y, "coordinate_y"),
            CriteriaApiUtils.generatePredicate(builder, root, creationDate, "creation_date"),
            CriteriaApiUtils.generatePredicate(builder, root, studentsCount, "students_count"),
            ltStudentsCount?.let { builder.lt(root.get<Int>("students_count"), it) },
            gtStudentsCount?.let { builder.gt(root.get<Int>("students_count"), it) },
            CriteriaApiUtils.generatePredicate(builder, root, formOfEducation, "form_of_education"),
            CriteriaApiUtils.generatePredicate(builder, root, semesterEnum?.name, "semester_enum"),
            groupAdmin?.let { builder.equal(root.get<String>("person").get<String>("id"), it) },
        ).toTypedArray()
    }
}