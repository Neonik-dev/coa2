package lisval.wrapper.controller

import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.http.server.ServerHttpRequest
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
    @RequestMapping(method = [RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE])
    fun proxy(request: ServerHttpRequest, @RequestBody(required = false) body: String?): ResponseEntity<String> {
//        val soapBody = buildSoapBody(body)
//        val targetUrl = buildTargetUrl(request)

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
        return ResponseEntity.ok("soapBody")
    }

    companion object {
        val logger = LoggerFactory.getLogger(this::class.java)
    }
}

//fun copyHeaders(
//    request: ServerHttpRequest,
//    target: HttpHeaders
//) {
//    request.headers.forEach { (name, values) ->
//        if (!name.equals(HttpHeaders.CONTENT_LENGTH, ignoreCase = true)) {
//            target.put(name, values)
//        }
//    } as (String, MutableList<String>) -> Unit
//}
//
//fun buildTargetUrl(request: HttpServletRequest): String {
//    val query = request.queryString?.let { "?$it" } ?: ""
//    return "https://soap-host.example.com${request.requestURI}$query"
//}