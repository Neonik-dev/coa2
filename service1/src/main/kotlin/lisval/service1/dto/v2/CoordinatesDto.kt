package lisval.service1.dto.v2

import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlType

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = ["x", "y"])
data class CoordinatesDto(
    @field:XmlElement(name = "x")
    val x: Int? = null,

    @field:XmlElement(name = "y", required = true)
    val y: Int,
) {
    constructor() : this(null, 0)
}