package lisval.service1.controller

import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import lisval.service1.dto.NewStudyGroup
import lisval.service1.dto.PageWrapper
import lisval.service1.dto.StudyGroupResponse
import lisval.service1.persistence.model.enums.FormOfEducation
import lisval.service1.persistence.model.enums.Semester
import lisval.service1.service.StudyGroupService
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/api/studygroups")
@Validated
class StudyGroupsController(
    private val studyGroupService: StudyGroupService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createGroup(@RequestBody @Valid request: NewStudyGroup) {
        studyGroupService.createGroup(request)
    }

    @GetMapping
    fun allGroup(
        @RequestParam sort: String?,
        @RequestParam @Min(0) page: Int = 0,
        @RequestParam @Min(0) size: Int = 20,
        @RequestParam @Min(0) id: Long?,
        @RequestParam x: String?,
        @RequestParam y: String?,
        @RequestParam creationDate: LocalDate?,
        @RequestParam studentsCount: Long?,
        @RequestParam formOfEducation: FormOfEducation?,
        @RequestParam semesterEnum: Semester?,
        @RequestParam groupAdmin: String?,
    ): PageWrapper<StudyGroupResponse> {
        return studyGroupService.getAll(sort, page, size, id, x, y, creationDate, studentsCount, formOfEducation, semesterEnum, groupAdmin)
    }


}