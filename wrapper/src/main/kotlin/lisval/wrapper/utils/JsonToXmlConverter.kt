package lisval.wrapper.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.xml.XmlMapper

object JsonToXmlConverter {

    private val jsonMapper = ObjectMapper()
    private val xmlMapper = XmlMapper().apply {
        configure(com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true)
    }

    fun convert(jsonStr: String, rootName: String = "root", prettyPrint: Boolean = true): String {
        val jsonNode = jsonMapper.readTree(jsonStr)
        val wrapper = mapOf(rootName to jsonNode)

        return if (prettyPrint) {
            xmlMapper.writerWithDefaultPrettyPrinter().writeValueAsString(wrapper)
        } else {
            xmlMapper.writeValueAsString(wrapper)
        }
    }

    fun convertFromObject(jsonObject: Any, rootName: String = "root", prettyPrint: Boolean = true): String {
        val wrapper = mapOf(rootName to jsonObject)

        return if (prettyPrint) {
            xmlMapper.writerWithDefaultPrettyPrinter().writeValueAsString(wrapper)
        } else {
            xmlMapper.writeValueAsString(wrapper)
        }
    }
}