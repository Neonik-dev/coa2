package lisval.service1.dto.v2

import jakarta.xml.bind.annotation.adapters.XmlAdapter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class LocalDateAdapter : XmlAdapter<String, LocalDate?>() {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    override fun marshal(v: LocalDate?): String {
        return v?.format(formatter) ?: ""
    }

    override fun unmarshal(v: String?): LocalDate? {
        return if (v.isNullOrBlank()) null else LocalDate.parse(v, formatter)
    }
}