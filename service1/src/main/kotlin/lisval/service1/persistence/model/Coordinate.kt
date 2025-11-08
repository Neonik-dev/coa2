package lisval.service1.persistence.model

import jakarta.persistence.Column
import jakarta.persistence.Embeddable


@Embeddable
data class Coordinate(
    @Column(name = "coordinate_x")
    var x: Int?,
    @Column(name = "coordinate_y", nullable = false)
    var y: Int,
)