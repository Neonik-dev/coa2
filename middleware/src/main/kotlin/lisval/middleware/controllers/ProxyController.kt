package lisval.middleware.controllers

import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient

@RestController
@RequestMapping("/**")
class ProxyController(
    private val webClient: WebClient,
) {

    @RequestMapping(
        method = [
            RequestMethod.GET,
            RequestMethod.POST,
            RequestMethod.PUT,
            RequestMethod.DELETE
        ]
    )
    fun proxy(
        request: HttpServletRequest,
        @RequestBody(required = false) body: String?
    ): ResponseEntity<String> {
        val soapBody = buildSoapBody(body)
        val targetUrl = buildTargetUrl(request)
        println(soapBody)
        logger.info(soapBody)

//        val response = webClient
//            .post()
//            .uri(targetUrl)
//            .headers { headers ->
//                copyHeaders(request, headers)
//                headers["Content-Type"] = "text/xml;charset=UTF-8"
//            }
//            .bodyValue(soapBody)
//            .retrieve()
//            .toEntity(String::class.java)
//            .block()

//        return ResponseEntity
//            .status(response!!.statusCode)
//            .headers(response.headers)
//            .body(response.body)
        return ResponseEntity.ok(soapBody)
    }

    companion object {
        val logger = LoggerFactory.getLogger(this::class.java)
    }
}

fun copyHeaders(
    request: HttpServletRequest,
    headers: HttpHeaders
) {
    request.headerNames.asSequence().forEach { name ->
        if (!name.equals("content-length", true)) {
            headers[name] = request.getHeaders(name).toString()
        }
    }
}

fun buildTargetUrl(request: HttpServletRequest): String {
    val query = request.queryString?.let { "?$it" } ?: ""
    return "https://soap-host.example.com${request.requestURI}$query"
}

fun buildSoapBody(restBody: String?): String {
    return """
        <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                          xmlns:ns="http://example.com/schema">
            <soapenv:Header/>
            <soapenv:Body>
                <ns:MyRequest>
                    <ns:payload><![CDATA[$restBody]]></ns:payload>
                </ns:MyRequest>
            </soapenv:Body>
        </soapenv:Envelope>
    """.trimIndent()
}