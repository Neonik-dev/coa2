package lisval.service1.mapper

import lisval.service1.dto.CoordinatesDto
import lisval.service1.dto.NewStudyGroup
import lisval.service1.dto.PageWrapper
import lisval.service1.dto.StudyGroupResponse
import lisval.service1.persistence.model.Coordinate
import lisval.service1.persistence.model.Person
import lisval.service1.persistence.model.StudyGroup
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class StudyGroupMapper(
    private val personMapper: PersonMapper,
) {

    fun mapToEntity(request: NewStudyGroup, admin: Person?): StudyGroup {
        return StudyGroup(
            id = -1,
            name = request.name,
            coordinates = Coordinate(
                x = request.coordinates.x,
                y = request.coordinates.y
            ),
            creationDate = LocalDate.now(),
            studentCount = request.studentsCount,
            formOfEducation = request.formOfEducation,
            semesterEnum = request.semesterEnum,
            groupAdmin = admin,
        )
    }

    fun mapToPageStudyGroupResponse(groups: List<StudyGroup>, currentPage: Int, totalPages: Int): PageWrapper<StudyGroupResponse> {
        return PageWrapper(
            data = groups.map { mapToStudyGroupResponse(it) },
            currentPage = currentPage,
            totalPages = totalPages,
        )
    }

    fun mapToStudyGroupResponse(group: StudyGroup): StudyGroupResponse {
        return StudyGroupResponse(
            id = group.id,
            name = group.name,
            coordinates = CoordinatesDto(
                x = group.coordinates.x,
                y = group.coordinates.y,
            ),
            creationDate = group.creationDate,
            studentsCount = group.studentCount,
            formOfEducation = group.formOfEducation,
            semesterEnum = group.semesterEnum,
            groupAdmin = group.groupAdmin?.let { personMapper.mapToPersonResponse(it) },
        )
    }
}