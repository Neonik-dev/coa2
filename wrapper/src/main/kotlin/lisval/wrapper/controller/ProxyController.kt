package lisval.wrapper.controller

import com.fasterxml.jackson.databind.ObjectMapper
import lisval.wrapper.utils.JsonToXmlConverter
import lisval.wrapper.utils.copyHeaders
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Mono
import java.net.URI

@RestController
@RequestMapping()
class ProxyController(
    private val webClient: WebClient
) {

    @RequestMapping("/**")
    fun proxyRequest(request: ServerHttpRequest): Mono<String> {
        val originalPath = request.path.toString()

        val targetUrl = UriComponentsBuilder
            .fromUri(URI.create("https://localhost:8051"))
            .path(originalPath)
            .queryParams(request.queryParams)
            .build()
            .toUri()
        val headers = copyHeaders(request.headers)
        val method = request.method
        val xmlBody: String = JsonToXmlConverter.convert(ObjectMapper().writeValueAsString(request.body))


        return webClient
            .method(method)
            .uri(targetUrl)
            .headers { it.addAll(headers) }
            .body(BodyInserters.fromPublisher(Mono.just(xmlBody), String::class.java))
            .retrieve()
            .bodyToMono(String::class.java)
    }
}