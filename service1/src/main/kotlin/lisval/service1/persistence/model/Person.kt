package lisval.service1.persistence.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import lisval.service1.persistence.model.enums.Country
import java.time.LocalDate
import java.util.UUID

@Entity
@Table(name = "person")
data class Person(
    @Id
    val id: String = UUID.randomUUID().toString(),
    @Column(nullable = false)
    val name: String,
    val birthday: LocalDate?,
    @Column(nullable = false)
    val weight: Int,
    @Column(name = "passport_id", unique = true)
    val passportId: String?,
    @Enumerated(EnumType.STRING)
    val nationality: Country?
)