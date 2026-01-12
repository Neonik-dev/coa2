package lisval.middleware.utils

@Component
class JsonToXmlConverter(
    private val objectMapper: ObjectMapper = jacksonObjectMapper(),
    private val xmlMapper: XmlMapper = XmlMapper()
) {

    fun convert(
        json: String,
        rootName: String,
        namespace: String? = null
    ): String {

        val jsonNode = objectMapper.readTree(json)

        val xmlMapperConfigured = xmlMapper.copy()
            .apply {
                setDefaultUseWrapper(false)
                enable(ToXmlGenerator.Feature.WRITE_XML_DECLARATION)
            }

        val writer = xmlMapperConfigured.writer().withRootName(
            if (namespace != null)
                QName(namespace, rootName)
            else
                rootName
        )

        return writer.writeValueAsString(jsonNode)
    }
}
