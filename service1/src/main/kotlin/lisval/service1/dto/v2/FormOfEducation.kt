package lisval.service1.dto.v2

import jakarta.xml.bind.annotation.adapters.XmlAdapter

enum class FormOfEducation {
    DISTANCE_EDUCATION,
    FULL_TIME_EDUCATION,
    EVENING_CLASSES,
    ;

    fun toXmlValue(): String {
        return when (this) {
            DISTANCE_EDUCATION -> "DISTANCE"
            FULL_TIME_EDUCATION -> "FULL_TIME"
            EVENING_CLASSES -> "EVENING"
        }
    }

    companion object {
        fun fromXmlValue(value: String): FormOfEducation {
            return when (value) {
                "DISTANCE" -> DISTANCE_EDUCATION
                "FULL_TIME" -> FULL_TIME_EDUCATION
                "EVENING" -> EVENING_CLASSES
                else -> throw IllegalArgumentException("Unknown FormOfEducation: $value")
            }
        }
    }
}

class FormOfEducationAdapter : XmlAdapter<String, FormOfEducation>() {
    override fun marshal(v: FormOfEducation?): String {
        return v?.toXmlValue() ?: ""
    }

    override fun unmarshal(v: String?): FormOfEducation? {
        return v?.let { FormOfEducation.fromXmlValue(it) }
    }
}