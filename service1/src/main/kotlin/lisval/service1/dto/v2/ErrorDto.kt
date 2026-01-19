package lisval.service1.dto.v2

import jakarta.xml.bind.annotation.*

@XmlRootElement(name = "Error")
@XmlAccessorType(XmlAccessType.FIELD)
data class ErrorDto(
    @field:XmlElement(name = "message", required = true)
    @field:XmlSchemaType(name = "string")
    val text: String,
) {
    constructor() : this("")
}
