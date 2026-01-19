package lisval.service1.dto.v2

import jakarta.xml.bind.annotation.adapters.XmlAdapter

enum class Semester {
    FIRST,
    THIRD,
    SIXTH,
    SEVENTH,
    EIGHTH,
    ;

    fun toXmlValue(): String {
        return when (this) {
            FIRST -> "FIRST"
            THIRD -> "THIRD"
            SIXTH -> "SIXTH"
            SEVENTH -> "SEVENTH"
            EIGHTH -> "EIGHTH"
        }
    }

    fun toNumber(): Int {
        return when (this) {
            FIRST -> 1
            THIRD -> 3
            SIXTH -> 6
            SEVENTH -> 7
            EIGHTH -> 8
        }
    }

    companion object {
        fun fromXmlValue(value: String): Semester {
            return when (value) {
                "FIRST" -> FIRST
                "THIRD" -> THIRD
                "SIXTH" -> SIXTH
                "SEVENTH" -> SEVENTH
                "EIGHTH" -> EIGHTH
                else -> throw IllegalArgumentException("Unknown Semester: $value")
            }
        }

        fun fromNumber(value: Int): Semester {
            return when (value) {
                1 -> FIRST
                3 -> THIRD
                6 -> SIXTH
                7 -> SEVENTH
                8 -> EIGHTH
                else -> throw IllegalArgumentException("No Semester for number: $value")
            }
        }
    }
}

class SemesterAdapter : XmlAdapter<String, Semester>() {
    override fun marshal(v: Semester?): String {
        return v?.toXmlValue() ?: ""
    }

    override fun unmarshal(v: String?): Semester? {
        return v?.let { Semester.fromXmlValue(it) }
    }
}

class SemesterNumberAdapter : XmlAdapter<Int, Semester>() {
    override fun marshal(v: Semester?): Int {
        return v?.toNumber() ?: 0
    }

    override fun unmarshal(v: Int?): Semester? {
        return v?.let { Semester.fromNumber(it) }
    }
}