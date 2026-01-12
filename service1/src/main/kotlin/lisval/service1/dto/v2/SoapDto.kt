package lisval.service1.dto.v2

import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlRootElement
import jakarta.xml.bind.annotation.XmlType
import java.time.LocalDate
import java.time.LocalDateTime

@XmlRootElement(name = "createPersonRequest")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "CreatePersonRequest",
    propOrder = ["name", "birthday", "weight", "passportId", "nationality"],
    namespace = "http://localhost/api/persons"
)
data class CreatePersonRequest(

    @field:XmlElement(required = true, namespace = "http://localhost/api/persons")
    val name: String? = null,

    @field:XmlElement(namespace = "http://localhost/api/persons")
    val birthday: LocalDate? = null,

    @field:XmlElement(required = true, namespace = "http://localhost/api/persons")
    val weight: Int? = null,

    @field:XmlElement(namespace = "http://localhost/api/persons")
    val passportId: String? = null,

    @field:XmlElement(namespace = "http://localhost/api/persons")
    val nationality: String? = null // Используем String для enum
)

@XmlRootElement(name = "createPersonResponse")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "CreatePersonResponse",
    propOrder = ["status", "message", "personId"],
    namespace = "http://localhost/api/persons"
)
data class CreatePersonResponse(

    @field:XmlElement(required = true, namespace = "http://localhost/api/persons")
    var status: String? = null,

    @field:XmlElement(required = true, namespace = "http://localhost/api/persons")
    var message: String? = null,

    @field:XmlElement(namespace = "http://localhost/api/persons")
    val personId: Long? = null
)

@XmlRootElement(name = "coordinates")
@XmlAccessorType(XmlAccessType.FIELD)
data class CoordinatesType(
    @XmlElement(required = true)
    val x: Double? = null,

    @XmlElement(required = true)
    val y: Int = 0
)

@XmlRootElement(name = "person")
@XmlAccessorType(XmlAccessType.FIELD)
data class PersonType(
    @XmlElement(required = true)
    val name: String? = null,

    @XmlElement(required = true)
    val birthday: LocalDate? = null,

    @XmlElement(required = true)
    val weight: Int? = null,

    @XmlElement
    val passportId: String? = null
)

@XmlRootElement(name = "studyGroupResponse")
@XmlAccessorType(XmlAccessType.FIELD)
data class StudyGroupResponseType(
    @XmlElement(required = true)
    val id: Long? = null,

    @XmlElement(required = true)
    val name: String? = null,

    @XmlElement(required = true)
    val coordinates: CoordinatesType? = null,

    @XmlElement(required = true)
    val creationDate: LocalDateTime? = null,

    @XmlElement(required = true)
    val studentsCount: Long? = null,

    @XmlElement(required = true)
    val formOfEducation: String? = null, // Enum как String

    @XmlElement(required = true)
    val semesterEnum: String? = null, // Enum как String

    @XmlElement
    val groupAdmin: PersonType? = null
)

@XmlRootElement(name = "groupByFormOfEducation")
@XmlAccessorType(XmlAccessType.FIELD)
data class GroupByFormOfEducationType(
    @XmlElement(required = true)
    val formOfEducation: String? = null,

    @XmlElement(required = true)
    val count: Long? = null
)

@XmlRootElement(name = "pageWrapper")
@XmlAccessorType(XmlAccessType.FIELD)
data class PageWrapperType(
    @XmlElement(required = true)
    val content: List<StudyGroupResponseType> = emptyList(),

    @XmlElement(required = true)
    val totalPages: Int? = null,

    @XmlElement(required = true)
    val totalElements: Long? = null,

    @XmlElement(required = true)
    val size: Int? = null,

    @XmlElement(required = true)
    val number: Int? = null
)

@XmlRootElement(name = "createGroupResponse")
@XmlAccessorType(XmlAccessType.FIELD)
data class CreateGroupResponse(
    @XmlElement(required = true, namespace = "http://localhost/api/studygroups")
    var status: String? = null,

    @XmlElement(required = true, namespace = "http://localhost/api/studygroups")
    var message: String? = null,

    @XmlElement(namespace = "http://localhost/api/studygroups")
    var groupId: Long? = null
)

@XmlRootElement(name = "allGroupsResponse")
@XmlAccessorType(XmlAccessType.FIELD)
data class AllGroupsResponse(
    @XmlElement(required = true, namespace = "http://localhost/api/studygroups")
    var pageData: PageWrapperType? = null
)

@XmlRootElement(name = "getByIdResponse")
@XmlAccessorType(XmlAccessType.FIELD)
data class GetByIdResponse(
    @XmlElement(required = true, namespace = "http://localhost/api/studygroups")
    val studyGroup: StudyGroupResponseType? = null
)

@XmlRootElement(name = "updateGroupResponse")
@XmlAccessorType(XmlAccessType.FIELD)
data class UpdateGroupResponse(
    @XmlElement(required = true, namespace = "http://localhost/api/studygroups")
    val status: String? = null,

    @XmlElement(required = true, namespace = "http://localhost/api/studygroups")
    val message: String? = null
)

@XmlRootElement(name = "deleteGroupResponse")
@XmlAccessorType(XmlAccessType.FIELD)
data class DeleteGroupResponse(
    @XmlElement(required = true, namespace = "http://localhost/api/studygroups")
    val status: String? = null,

    @XmlElement(required = true, namespace = "http://localhost/api/studygroups")
    val message: String? = null
)

@XmlRootElement(name = "minCreationDateResponse")
@XmlAccessorType(XmlAccessType.FIELD)
data class MinCreationDateResponse(
    @XmlElement(required = true, namespace = "http://localhost/api/studygroups")
    val studyGroup: StudyGroupResponseType? = null
)

@XmlRootElement(name = "groupByFormOfEducationResponse")
@XmlAccessorType(XmlAccessType.FIELD)
data class GroupByFormOfEducationResponse(
    @XmlElement(required = true, namespace = "http://localhost/api/studygroups")
    val groups: List<GroupByFormOfEducationType> = emptyList()
)

@XmlRootElement(name = "getLtFormOfEducationResponse")
@XmlAccessorType(XmlAccessType.FIELD)
data class GetLtFormOfEducationResponse(
    @XmlElement(required = true, namespace = "http://localhost/api/studygroups")
    val studyGroups: List<StudyGroupResponseType> = emptyList()
)

@XmlRootElement(name = "createGroupRequest")
@XmlAccessorType(XmlAccessType.FIELD)
data class CreateGroupRequest(
    @XmlElement(required = true, namespace = "http://localhost/api/studygroups")
    val name: String? = null,

    @XmlElement(required = true, namespace = "http://localhost/api/studygroups")
    val x: Double? = null,

    @XmlElement(required = true, namespace = "http://localhost/api/studygroups")
    val y: Long? = null,

    @XmlElement(required = true, namespace = "http://localhost/api/studygroups")
    val studentsCount: Long? = null,

    @XmlElement(required = true, namespace = "http://localhost/api/studygroups")
    val formOfEducation: String? = null,

    @XmlElement(required = true, namespace = "http://localhost/api/studygroups")
    val semesterEnum: String? = null,

    @XmlElement(namespace = "http://localhost/api/studygroups")
    val groupAdmin: PersonType? = null
)

@XmlRootElement(name = "allGroupsRequest")
@XmlAccessorType(XmlAccessType.FIELD)
data class AllGroupsRequest(
    @XmlElement(namespace = "http://localhost/api/studygroups")
    val sort: String? = null,

    @XmlElement(namespace = "http://localhost/api/studygroups")
    val page: Int? = null,

    @XmlElement(namespace = "http://localhost/api/studygroups")
    val size: Int? = null,

    @XmlElement(namespace = "http://localhost/api/studygroups")
    val id: Long? = null,

    @XmlElement(namespace = "http://localhost/api/studygroups")
    val name: String? = null,

    @XmlElement(namespace = "http://localhost/api/studygroups")
    val x: String? = null,

    @XmlElement(namespace = "http://localhost/api/studygroups")
    val y: String? = null,

    @XmlElement(namespace = "http://localhost/api/studygroups")
    val creationDate: LocalDate? = null,

    @XmlElement(namespace = "http://localhost/api/studygroups")
    val studentsCount: Long? = null,

    @XmlElement(namespace = "http://localhost/api/studygroups")
    val ltStudentsCount: Long? = null,

    @XmlElement(namespace = "http://localhost/api/studygroups")
    val gtStudentsCount: Long? = null,

    @XmlElement(namespace = "http://localhost/api/studygroups")
    val formOfEducation: String? = null,

    @XmlElement(namespace = "http://localhost/api/studygroups")
    val semesterEnum: String? = null,

    @XmlElement(namespace = "http://localhost/api/studygroups")
    val groupAdminName: String? = null
)

@XmlRootElement(name = "getByIdRequest")
@XmlAccessorType(XmlAccessType.FIELD)
data class GetByIdRequest(
    @XmlElement(required = true, namespace = "http://localhost/api/studygroups")
    val id: Long? = null
)

@XmlRootElement(name = "updateGroupRequest")
@XmlAccessorType(XmlAccessType.FIELD)
data class UpdateGroupRequest(
    @XmlElement(required = true, namespace = "http://localhost/api/studygroups")
    val id: Long? = null,

    @XmlElement(required = true, namespace = "http://localhost/api/studygroups")
    val groupData: CreateGroupRequest? = null
)

@XmlRootElement(name = "deleteGroupRequest")
@XmlAccessorType(XmlAccessType.FIELD)
data class DeleteGroupRequest(
    @XmlElement(required = true, namespace = "http://localhost/api/studygroups")
    val id: Long? = null
)

@XmlRootElement(name = "minCreationDateRequest")
@XmlAccessorType(XmlAccessType.FIELD)
class MinCreationDateRequest

@XmlRootElement(name = "groupByFormOfEducationRequest")
@XmlAccessorType(XmlAccessType.FIELD)
class GroupByFormOfEducationRequest

@XmlRootElement(name = "getLtFormOfEducationRequest")
@XmlAccessorType(XmlAccessType.FIELD)
data class GetLtFormOfEducationRequest(
    @XmlElement(required = true, namespace = "http://localhost/api/studygroups")
    val formOfEducation: String? = null
)
