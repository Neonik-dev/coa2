package lisval.wrapper.utils

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class UniversalSoapProxyService(
    private val restTemplate: RestTemplate,
    private val objectMapper: ObjectMapper
) {

    fun callSoapService(
        soapEndpointUrl: String,
        soapAction: String,
        operationName: String,
        namespace: String,
        restJsonBody: String,
        soapHeaders: Map<String, String> = emptyMap(),
        customNamespaces: Map<String, String> = emptyMap()
    ): ResponseEntity<String> {

        val soapRequest = buildSoapRequest(
            operationName = operationName,
            namespace = namespace,
            jsonBody = restJsonBody,
            soapHeaders = soapHeaders,
            customNamespaces = customNamespaces
        )

        val httpHeaders = HttpHeaders().apply {
            contentType = MediaType.TEXT_XML
            set("SOAPAction", soapAction)
            soapHeaders.forEach { (key, value) ->
                set(key, value)
            }
        }

        val requestEntity = HttpEntity(soapRequest, httpHeaders)
        return restTemplate.exchange(
            soapEndpointUrl,
            HttpMethod.POST,
            requestEntity,
            String::class.java
        )
    }


    private fun buildSoapRequest(
        operationName: String,
        namespace: String,
        jsonBody: String,
        soapHeaders: Map<String, String>,
        customNamespaces: Map<String, String>
    ): String {
        val jsonNode = objectMapper.readTree(jsonBody)
        val requestBodyXml = jsonToXmlElement(jsonNode, namespace)
        return """
            <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                ${buildNamespaceDeclarations(namespace, customNamespaces)}>
                <soapenv:Header>
                    ${buildSoapHeaders(soapHeaders)}
                </soapenv:Header>
                <soapenv:Body>
                    <${operationName} xmlns="${namespace}">
                        ${requestBodyXml}
                    </${operationName}>
                </soapenv:Body>
            </soapenv:Envelope>
        """.trimIndent()
    }

    private fun jsonToXmlElement(node: JsonNode, namespace: String? = null): String {
        return when {
            node.isObject -> {
                val fields = node.fields()
                buildString {
                    while (fields.hasNext()) {
                        val (fieldName, fieldValue) = fields.next()
                        append("<${escapeXml(fieldName)}>")
                        append(jsonToXmlElement(fieldValue, namespace))
                        append("</${escapeXml(fieldName)}>")
                    }
                }
            }
            node.isArray -> {
                buildString {
                    node.forEach { item ->
                        append("<item>")
                        append(jsonToXmlElement(item, namespace))
                        append("</item>")
                    }
                }
            }
            node.isValueNode -> escapeXml(node.asText())
            else -> ""
        }
    }

    private fun escapeXml(text: String): String {
        return text.replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&apos;")
    }

    private fun buildNamespaceDeclarations(
        mainNamespace: String,
        customNamespaces: Map<String, String>
    ): String {
        val namespaces = mutableListOf<String>()
        namespaces.add("xmlns:tem=\"$mainNamespace\"")
        customNamespaces.forEach { (prefix, uri) ->
            namespaces.add("xmlns:$prefix=\"$uri\"")
        }

        return namespaces.joinToString(" ")
    }

    private fun buildSoapHeaders(headers: Map<String, String>): String {
        if (headers.isEmpty()) return ""

        return buildString {
            headers.forEach { (key, value) ->
                appendLine("<$key xmlns=\"http://tempuri.org/\">$value</$key>")
            }
        }
    }
}