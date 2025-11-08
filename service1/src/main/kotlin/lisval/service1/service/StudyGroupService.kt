package lisval.service1.service

import jakarta.persistence.EntityManager
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import lisval.service1.dto.NewStudyGroup
import lisval.service1.dto.PageWrapper
import lisval.service1.dto.StudyGroupResponse
import lisval.service1.mapper.StudyGroupMapper
import lisval.service1.persistence.model.StudyGroup
import lisval.service1.persistence.model.enums.FormOfEducation
import lisval.service1.persistence.model.enums.Semester
import lisval.service1.persistence.repository.PersonRepository
import lisval.service1.persistence.repository.StudyGroupRepository
import lisval.service1.utils.CriteriaApiUtils
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDate
import kotlin.math.ceil

@Service
class StudyGroupService(
    private val studyGroupRepository: StudyGroupRepository,
    private val personRepository: PersonRepository,
    private val studentGroupMapper: StudyGroupMapper,
    private val entityManager: EntityManager,
    ) {

    fun createGroup(request: NewStudyGroup) {
        val admin = request.groupAdmin?.let {
            personRepository.findByIdOrNull(request.groupAdmin) ?: throw RuntimeException("челик не найден")
        }
        val studyGroup = studentGroupMapper.mapToEntity(request, admin)
        studyGroupRepository.save(studyGroup)
    }

    fun getById(id: Long): StudyGroup {
        return studyGroupRepository.findByIdOrNull(id) ?: throw RuntimeException("челик не найден")
    }

    fun removeById(id: Long) {
        return studyGroupRepository.deleteById(id)
    }

    fun getByMinCreationDate() : StudyGroup {
        return studyGroupRepository.findFirstByOrderByCreationDateAsc() ?: throw RuntimeException("в бд еще нет ни одного челика")
    }

    fun getAll(
        sort: String?,
        page: Int,
        size: Int,
        id: Long?,
        x: String?,
        y: String?,
        creationDate: LocalDate?,
        studentsCount: Long?,
        formOfEducation: FormOfEducation?,
        semesterEnum: Semester?,
        groupAdmin: String?,
    ): PageWrapper<StudyGroupResponse> {
        val builder = entityManager.criteriaBuilder

        val countQuery = builder.createQuery(Long::class.java)
        var root = countQuery.from(StudyGroup::class.java)
        var predicates = generatePredicates(builder, root, id, x, y, creationDate, studentsCount, formOfEducation, semesterEnum, groupAdmin)
        countQuery.select(builder.count(root)).where(*predicates)
        val countRaw = entityManager.createQuery(countQuery).singleResult
        val countPage = ceil(countRaw / size.toDouble()).toInt()

        val criteriaQuery = builder.createQuery(StudyGroup::class.java)
        root = criteriaQuery.from(StudyGroup::class.java)
        predicates = generatePredicates(builder, root, id, x, y, creationDate, studentsCount, formOfEducation, semesterEnum, groupAdmin)
        val sortPredicates = CriteriaApiUtils.generateSortPredicates(builder, root, sort)
        val select = criteriaQuery.select(root).where(*predicates).orderBy(sortPredicates)
        val persons = entityManager.createQuery(select).setFirstResult((page) * size).setMaxResults(size).resultList
        return studentGroupMapper.mapToPageStudyGroupResponse(persons, page, countPage)
    }

    private fun generatePredicates(
        builder: CriteriaBuilder,
        root: Root<StudyGroup>,
        id: Long?,
        x: String?,
        y: String?,
        creationDate: LocalDate?,
        studentsCount: Long?,
        formOfEducation: FormOfEducation?,
        semesterEnum: Semester?,
        groupAdmin: String?,
    ): Array<Predicate> {
        return listOfNotNull(
            CriteriaApiUtils.generatePredicate(builder, root, id, "id"),
            CriteriaApiUtils.generatePredicate(builder, root, x, "coordinate_x"),
            CriteriaApiUtils.generatePredicate(builder, root, y, "coordinate_y"),
            CriteriaApiUtils.generatePredicate(builder, root, creationDate, "creation_date"),
            CriteriaApiUtils.generatePredicate(builder, root, studentsCount, "students_count"),
            CriteriaApiUtils.generatePredicate(builder, root, formOfEducation, "form_of_education"),
            CriteriaApiUtils.generatePredicate(builder, root, semesterEnum?.name, "semester_enum"),
            groupAdmin?.let { builder.equal(root.get<String>("person").get<String>("id"), it) },
        ).toTypedArray()
    }
}