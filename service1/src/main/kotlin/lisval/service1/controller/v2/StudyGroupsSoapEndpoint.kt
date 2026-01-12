package lisval.service1.controller.v2

import lisval.service1.dto.v2.AllGroupsRequest
import lisval.service1.dto.v2.AllGroupsResponse
import lisval.service1.dto.v2.CoordinatesType
import lisval.service1.dto.v2.CreateGroupRequest
import lisval.service1.dto.v2.CreateGroupResponse
import lisval.service1.dto.v2.GetByIdRequest
import lisval.service1.dto.v2.GetByIdResponse
import lisval.service1.dto.v2.PageWrapperType
import lisval.service1.dto.v2.PersonType
import lisval.service1.dto.v2.StudyGroupResponseType
import lisval.service1.mapper.SoapRequestMapper
import lisval.service1.mapper.StudyGroupMapper
import lisval.service1.persistence.model.enums.FormOfEducation
import lisval.service1.persistence.model.enums.Semester
import lisval.service1.service.StudyGroupService
import lisval.service1.utils.ParamValidation
import org.springframework.ws.server.endpoint.annotation.*
import org.springframework.ws.soap.server.endpoint.annotation.SoapAction

@Endpoint
class StudyGroupsSoapEndpoint(
    private val studyGroupService: StudyGroupService,
    private val studyGroupMapper: StudyGroupMapper,
    private val soapRequestMapper: SoapRequestMapper
) {

    companion object {
        private const val NAMESPACE_URI = "http://yourcompany.com/api/studygroups"
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "createGroupRequest")
    @ResponsePayload
    @SoapAction("$NAMESPACE_URI/createGroup")
    fun createGroup(@RequestPayload request: CreateGroupRequest): CreateGroupResponse {
        val newStudyGroup = soapRequestMapper.mapCreateGroupRequestToDto(request)
        val groupId = studyGroupService.createGroup(newStudyGroup)

        return CreateGroupResponse().apply {
            status = "SUCCESS"
            message = "Study group created successfully"
            this.groupId = groupId as Long
        }
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "allGroupsRequest")
    @ResponsePayload
    @SoapAction("$NAMESPACE_URI/getAllGroups")
    fun allGroups(@RequestPayload request: AllGroupsRequest): AllGroupsResponse {
        ParamValidation.validate(
            request.studentsCount,
            request.ltStudentsCount,
            request.gtStudentsCount
        )

        val pageWrapper = studyGroupService.getAll(
            sort = request.sort,
            page = request.page ?: 0,
            size = request.size ?: 20,
            id = request.id,
            name = request.name,
            x = request.x,
            y = request.y,
            creationDate = request.creationDate,
            studentsCount = request.studentsCount,
            ltStudentsCount = request.ltStudentsCount,
            gtStudentsCount = request.gtStudentsCount,
            formOfEducation = request.formOfEducation?.let { FormOfEducation.valueOf(it) },
            semesterEnum = request.semesterEnum?.let { Semester.valueOf(it) },
            groupAdmin = request.groupAdminName
        )

        val pageWrapperType = PageWrapperType(
            content = pageWrapper.data.map { studyGroup ->
                StudyGroupResponseType(
                    id = studyGroup.id,
                    name = studyGroup.name,
                    coordinates = CoordinatesType(
                        x = studyGroup.coordinates.x?.toDouble(),
                        y = studyGroup.coordinates.y
                    ),
                    creationDate = null,
                    studentsCount = studyGroup.studentsCount,
                    formOfEducation = studyGroup.formOfEducation?.name,
                    semesterEnum = studyGroup.semesterEnum?.name,
                    groupAdmin = studyGroup.groupAdmin?.let { admin ->
                        PersonType(
                            name = admin.name,
                            birthday = null,
                            weight = admin.weight,
                            passportId = admin.passportId
                        )
                    }
                )
            },
            totalPages = pageWrapper.totalPages,
            totalElements = pageWrapper.totalPages.toLong(),
            size = pageWrapper.data.size,
            number = pageWrapper.data.size
        )

        return AllGroupsResponse().apply {
            pageData = pageWrapperType
        }
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getByIdRequest")
    @ResponsePayload
    @SoapAction("$NAMESPACE_URI/getGroupById")
    fun getById(@RequestPayload request: GetByIdRequest): GetByIdResponse {
        var studyGroup = studyGroupService.getById(request.id!!)

        val response = StudyGroupResponseType(
            id = studyGroup.id,
            name = studyGroup.name,
            coordinates = CoordinatesType(
                x = studyGroup.coordinates.x as Double?,
                y = studyGroup.coordinates.y
            ),
            creationDate = null,
            studentsCount = studyGroup.studentCount,
            formOfEducation = studyGroup.formOfEducation?.name,
            semesterEnum = studyGroup.semesterEnum?.name,
            groupAdmin = studyGroup.groupAdmin?.let { admin ->
                PersonType(
                    name = admin.name,
                    birthday = admin.birthday,
                    weight = admin.weight,
                    passportId = admin.passportId
                )
            }
        )

        return GetByIdResponse().apply {
            studyGroup = studyGroup
        }
    }
}