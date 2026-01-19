package lisval.service1.dto.v2

import jakarta.xml.bind.annotation.adapters.XmlAdapter

enum class Country {
    FRANCE,
    CHINA,
    INDIA,
    ;

    fun toXmlValue(): String {
        return when (this) {
            FRANCE -> "FRANCE"
            CHINA -> "CHINA"
            INDIA -> "INDIA"
        }
    }

    fun toCode(): String {
        return when (this) {
            FRANCE -> "FR"
            CHINA -> "CN"
            INDIA -> "IN"
        }
    }

    companion object {
        fun fromXmlValue(value: String): Country {
            return when (value.uppercase()) {
                "FRANCE", "FR" -> FRANCE
                "CHINA", "CN" -> CHINA
                "INDIA", "IN" -> INDIA
                else -> throw IllegalArgumentException("Unknown Country: $value")
            }
        }
    }
}

class CountryAdapter : XmlAdapter<String, Country>() {
    override fun marshal(v: Country?): String {
        return v?.toXmlValue() ?: ""
    }

    override fun unmarshal(v: String?): Country? {
        return if (v.isNullOrBlank()) null else Country.fromXmlValue(v)
    }
}

class CountryCodeAdapter : XmlAdapter<String, Country>() {
    override fun marshal(v: Country?): String {
        return v?.toCode() ?: ""
    }

    override fun unmarshal(v: String?): Country? {
        return if (v.isNullOrBlank()) null else Country.fromXmlValue(v)
    }
}