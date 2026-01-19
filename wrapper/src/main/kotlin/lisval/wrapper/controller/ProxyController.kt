package lisval.wrapper.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import lisval.wrapper.utils.UniversalSoapProxyService
import org.springframework.http.ResponseEntity
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Mono
import java.net.URI
import org.springframework.http.*

@RestController
@RequestMapping()
class ProxyController(
    private val webClient: WebClient,
    private val objectMapper: ObjectMapper,
    private val xmlMapper: XmlMapper,
    private val soapProxyService: UniversalSoapProxyService
) {

    @PostMapping("/**")
    fun proxyToSoap(
        @RequestHeader("X-SOAP-Endpoint") soapEndpoint: String,
        @RequestHeader("X-SOAP-Action") soapAction: String,
        @RequestHeader("X-SOAP-Operation") operationName: String,
        @RequestHeader("X-SOAP-Namespace") namespace: String,
        @RequestHeader headers: HttpHeaders,
        @RequestBody restJsonBody: String
    ): ResponseEntity<String> {
        return soapProxyService.callSoapService(
            soapEndpointUrl = soapEndpoint,
            soapAction = soapAction,
            operationName = operationName,
            namespace = namespace,
            restJsonBody = restJsonBody,
            soapHeaders = headers.toSingleValueMap(),
        )
    }

    @RequestMapping("/test")
    fun proxyRequest(request: ServerHttpRequest, @RequestBody body: String): Mono<String> {
        val originalPath = request.path.toString()

        val targetUrl = UriComponentsBuilder
            .fromUri(URI.create("https://localhost:8051"))
            .path(originalPath)
            .queryParams(request.queryParams)
            .build()
            .toUri()

        val jsonBody = objectMapper.readTree(body)
        val xmlBody = xmlMapper.writeValueAsString(jsonBody)

        val method = request.method

        return webClient
            .method(method)
            .uri(targetUrl)
            .headers { it.addAll(request.headers) }
            .body(BodyInserters.fromPublisher(Mono.just(xmlBody), String::class.java))
            .retrieve()
            .bodyToMono(String::class.java)
    }
}