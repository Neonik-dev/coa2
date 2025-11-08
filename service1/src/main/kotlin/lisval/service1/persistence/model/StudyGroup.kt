package lisval.service1.persistence.model

import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import lisval.service1.persistence.model.enums.FormOfEducation
import lisval.service1.persistence.model.enums.Semester
import java.time.LocalDate


@Entity
@Table(name = "study_group")
data class StudyGroup(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null,
    @Column(nullable = false)
    var name: String,
    @Embedded
    val coordinates: Coordinate,
    @Column(name = "creation_date", nullable = false)
    val creationDate: LocalDate,
    @Column(name = "student_count")
    var studentCount: Long?,
    @Column(name = "form_of_education")
    @Enumerated(EnumType.STRING)
    var  formOfEducation: FormOfEducation?,
    @Column(name = "semester_enum")
    @Enumerated(EnumType.STRING)
    var semesterEnum: Semester?,
    @JoinColumn(name = "group_admin")
    @ManyToOne
    var groupAdmin: Person?,
)